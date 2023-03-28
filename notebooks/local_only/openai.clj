(ns local-only.openai
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clerk-word-pca.files :as files]))


(def default-params
  {:key         (System/getenv "OPENAI_KEY")
   :model       "text-embedding-ada-002"})

(defn request [params]
  (let [params (merge default-params params)
        resp
        (try
          (http/post
           "https://api.openai.com/v1/embeddings"
           {:content-type :json
            :accept       :json
            :headers      {"Authorization" (str "Bearer " (:key params))}
            :body         (json/encode {:model       (:model params)
                                        :input       (:input params)})})
          (catch clojure.lang.ExceptionInfo e
            (str "`Error: " (-> e Throwable->map :cause) "`")))]
    (if (:body resp)
      {:success true :resp resp}
      {:success false :resp resp})))


(defn get-embeddings [sentences]
  (as-> sentences $
    {:input $}
    (request $)
    (:resp $)
    (:body $)
    (json/parse-string $ keyword)
    (:data $)
    (mapv :embedding $)))
(comment


  (spit "sentence-embeddings.edn" (pr-str (get-embeddings (files/get-lines-from-file "sentences.txt"))))
  (spit "sentence-embeddings2.edn" (pr-str (get-embeddings (files/get-lines-from-file "sentences2.txt"))))
  (spit "progressively-dissimilar-sentences-embeddings.edn" (pr-str (get-embeddings (files/get-lines-from-file "progressively-dissimilar-sentences.txt"))))
  (spit "progressively-dissimilar-sentences-embeddings2.edn" (pr-str (get-embeddings (files/get-lines-from-file "progressively-dissimilar-sentences2.txt"))))
  )