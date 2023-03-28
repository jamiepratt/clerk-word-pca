(ns distance 
  (:require [clojure.math.numeric-tower :as math]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [nextjournal.clerk :as clerk]
            [clojure.string :as str]))

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
  (->>
   vector
   (reduce #(+ (math/expt %2 2) %1) 0)
   (math/sqrt)))

(defn cosine-similarity
  "Calculate the cosine similarity between two vectors."
  [vector-a vector-b]
  (/ (dot-product vector-a vector-b) (* (magnitude vector-a) (magnitude vector-b))))


(comment 
;; ; Example usage
  (def vector-1 [1.0 2.0 3.0])
  
  (def vector-2 [4.0 5.0 6.0])
  

  (println "Euclidean distance:" (euclidean-distance vector-1 vector-2))
  
  (println "Cosine similarity:" (cosine-similarity vector-1 vector-2))
  )


(def sentences
  (->> "sentences2.txt"
       io/resource
       slurp
       str/split-lines))

; Load embeddings from a file
(def sentence-embeddings
  (->> "sentence-embeddings2.txt"
       io/resource
       slurp
       edn/read-string))

; Compare the last word embedding to all other embeddings using cosine similarity and Euclidean distance
(def cosine-similarities (map (partial cosine-similarity (last sentence-embeddings)) sentence-embeddings))
(def euclidean-distances (map (partial euclidean-distance (last sentence-embeddings)) sentence-embeddings ))

(clerk/table
 {:clerk/width :wide}
 {:head ["Cosine similarities" "Euclidean distances" "Sentences"]
  :rows (reverse (map list cosine-similarities euclidean-distances sentences))})