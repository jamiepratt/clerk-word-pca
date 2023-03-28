(ns local-only.openai
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [clojure.java.io :as io]))


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

(def sentences 
  (-> "sentences.txt"
      io/resource
      slurp
      str/split-lines
      vec))

sentences

(def api-results (request {:input sentences}))

(def embeddings (-> api-results
                    :resp
                    :body
                    (json/parse-string keyword)
                    :data
                    ((partial mapv :embedding))))

(spit "sentence-embeddings2.txt" (pr-str embeddings))
