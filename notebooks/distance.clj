(ns distance 
  (:require [clojure.math.numeric-tower :as math]
            [nextjournal.clerk :as clerk]
            [clerk-word-pca.files :as files]))

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


(defn distance-to-reference-embedding
  "Calculate the distance between a sentence embedding and a reference embedding."
  [method reference-embedding sentence-embeddings]
  (map (partial method reference-embedding) sentence-embeddings))

; Load embeddings from a file
(def sentence-embeddings (files/get-edn-from-file "sentence-embeddings2.edn"))

; ## sentence-embeddings2.edn

(clerk/table
 {:clerk/width :wide}
 {:head ["Cosine similarities" "Euclidean distances" "Sentences"]
  :rows (mapv
         vector
         (distance-to-reference-embedding cosine-similarity
                                          (last sentence-embeddings)
                                          (reverse sentence-embeddings))
         (distance-to-reference-embedding euclidean-distance
                                          (last sentence-embeddings)
                                          (reverse sentence-embeddings))
         (reverse (files/get-lines-from-file "sentences2.txt")))})

; ## progressively disimilar sentences

(def progressively-dissimilar-sentences-embeddings 
  (files/get-edn-from-file "progressively-dissimilar-sentences-embeddings.edn"))


(def progressively-dissimilar-sentences
  (files/get-lines-from-file "progressively-dissimilar-sentences.txt"))

(clerk/table
 {:clerk/width :wide}
 {:head ["Cosine similarities" "Euclidean distances" "Sentences"]
  :rows (mapv
         vector
         (distance-to-reference-embedding cosine-similarity
                                          (first progressively-dissimilar-sentences-embeddings)
                                          progressively-dissimilar-sentences-embeddings)
         (distance-to-reference-embedding euclidean-distance
                                          (first progressively-dissimilar-sentences-embeddings)
                                          progressively-dissimilar-sentences-embeddings)
         progressively-dissimilar-sentences)})

(def progressively-dissimilar-sentences-embeddings2
  (files/get-edn-from-file "progressively-dissimilar-sentences-embeddings2.edn"))

(def progressively-dissimilar-sentences2
  (files/get-lines-from-file "progressively-dissimilar-sentences2.txt"))

(clerk/table
 {:clerk/width :wide}
 {:head ["Cosine similarities" "Euclidean distances" "Sentences"]
  :rows (mapv
         vector
         (distance-to-reference-embedding cosine-similarity
                                          (first progressively-dissimilar-sentences-embeddings2)
                                          progressively-dissimilar-sentences-embeddings2)
         (distance-to-reference-embedding euclidean-distance
                                          (first progressively-dissimilar-sentences-embeddings2)
                                          progressively-dissimilar-sentences-embeddings2)
         progressively-dissimilar-sentences2)})