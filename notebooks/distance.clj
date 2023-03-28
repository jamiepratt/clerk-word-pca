(ns distance 
  (:require [clojure.math.numeric-tower :as math]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn euclidean-distance
  "Calculate the Euclidean distance between two vectors."
  [vector-a vector-b]
  (->> (map - vector-a vector-b)
       (map #(math/expt % 2))
       (reduce +)
       (math/sqrt)))

(defn dot-product
  "Calculate the dot product of two vectors."
  [vector-a vector-b]
  (reduce + (map * vector-a vector-b)))

(defn magnitude
  "Calculate the magnitude of a vector."
  [vector]
  (->> (map #(math/expt % 2) vector)
       (reduce +)
       (math/sqrt)))

(defn cosine-similarity
  "Calculate the cosine similarity between two vectors."
  [vector-a vector-b]
  (let [dot-product (dot-product vector-a vector-b)
        magnitude-a (magnitude vector-a)
        magnitude-b (magnitude vector-b)]
    (/ dot-product (* magnitude-a magnitude-b))))

(defn word-embedding-distance
  "Find the distance between two word embedding vectors using the specified method (euclidean or cosine)."
  [vector-a vector-b method]
  (cond
    (= method :euclidean) (euclidean-distance vector-a vector-b)
    (= method :cosine) (cosine-similarity vector-a vector-b)
    :else (throw (IllegalArgumentException. "Invalid distance method"))))

(comment 
;; ; Example usage
  (def vector-1 [1.0 2.0 3.0])
  
  (def vector-2 [4.0 5.0 6.0])
  

  (println "Euclidean distance:" (word-embedding-distance vector-1 vector-2 :euclidean))
  
  (println "Cosine similarity:" (word-embedding-distance vector-1 vector-2 :cosine))
  )


(defn compare-embeddings
  "Compare the last word embedding to all other embeddings using the specified method (euclidean or cosine)."
  [embeddings method]
  (let [last-embedding (last embeddings)
        other-embeddings (butlast embeddings)
        distances (map #(word-embedding-distance last-embedding % method) other-embeddings)]
    distances))

; Load embeddings from a file
(def sentence-embeddings
  (->> "sentence-embeddings2.txt"
       io/resource
       slurp
       edn/read-string))

; Compare the last word embedding to all other embeddings using cosine similarity and Euclidean distance
(def cosine-similarities (compare-embeddings sentence-embeddings :cosine))
(def euclidean-distances (compare-embeddings sentence-embeddings :euclidean))

(println "Cosine similarities:" cosine-similarities)
(println "Euclidean distances:" euclidean-distances)