^{:nextjournal.clerk/visibility #{:hide-ns :hide}}
(ns ^:nextjournal.clerk/no-cache sentences2-pca-ploty
  (:require [nextjournal.clerk :as clerk]
            [nextjournal.clerk-slideshow :as slideshow]
            [incanter.core :as incanter]
            [incanter.stats :as stats]
            [clojure.core.matrix :as m]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]))



^{::clerk/viewer clerk/hide-result}
(clerk/add-viewers! [slideshow/viewer])


;; # Example of performing PCA on word and sentence embedding data


(def sentence-embeddings
  (->> "sentence-embeddings2.txt"
       io/resource
       slurp
       edn/read-string))

(def sentences
  (->> "sentences2.txt"
      io/resource
      slurp
      str/split-lines))


(def pca (stats/principal-components sentence-embeddings))
(def components (:rotation pca))
(def pc1 (incanter/sel components :cols 0))
(def pc2 (incanter/sel components :cols 1))
(def pc3 (incanter/sel components :cols 2))
(def x (m/mmul sentence-embeddings pc1))
(def y (m/mmul sentence-embeddings pc2))
(def z (m/mmul sentence-embeddings pc3))

(clerk/plotly 
 {:clerk/width :full
  :clerk/height 500}
 {:data [{:x x :y y :z z :type "scatter3d" :mode "markers"
          :text sentences}]})

