(ns user
  (:require [nextjournal.clerk :as clerk]
            [portal.api :as p]))

;; start Clerk's built-in webserver on the default port 7777, opening the browser when done
(clerk/serve! {:watch-paths ["notebooks" "src"]})

(defmacro t> [& expr]
  `(let [start# (. System (nanoTime))
         result# (do
                   ~@expr)
         end# (. System (nanoTime))
         total-time# (/ (double (- end# start#)) 100000.0)]
     (binding [*print-meta* true]
       (tap> {:expr expr :t total-time# :returns return# :env &env}))
     result#))
(let [p (p/open)]
  (add-tap #'p/submit)
  p)
(comment
  ;; to require tests:
  (require '[dbinomial-test])
  (clerk/serve! {:watch-paths ["notebooks" "src"]})
  ;; start without file watcher, open browser when started
  (clerk/serve! {:browse? true})

  ;; start with file watcher for these sub-directory paths
  (clerk/serve! {:watch-paths ["notebooks" "src" "index.md"]})

  ;; start with file watcher and a `show-filter-fn` function to watch
  ;; a subset of notebooks
  (clerk/serve! {:watch-paths ["notebooks" "src"] :show-filter-fn #(clojure.string/starts-with? % "notebooks")})
  (clerk/serve! {:browse? false})
  (clerk/halt-watcher!)
  (clerk/clear-cache!)
  (clerk/halt!)

  ;; or call `clerk/show!` explicitly
  (clerk/show! "notebooks/words_pca_ploty.clj")
  (clerk/show! "notebooks/local_only/openai.clj")
  (clerk/show! "notebooks/distance.clj")

  (clerk/show! "notebooks/sentences_pca_ploty.clj")
  (clerk/show! "notebooks/sentences2_pca_ploty.clj")

  (clerk/show! "index.md")

  ;; TODO If you would like more details about how Clerk works, here's a
  ;; notebook with some implementation details.
  ;; (clerk/show! "notebooks/how_clerk_works.clj")

  (clerk/build-static-app! {:paths ["notebooks/**"] :bundle? false})
  )
