^{:nextjournal.clerk/visibility #{:hide-ns :hide}}
(ns ^:nextjournal.clerk/no-cache words-pca-ploty
  (:require [nextjournal.clerk :as clerk]
            [nextjournal.clerk-slideshow :as slideshow]
            [incanter.core :as incanter]
            [incanter.stats :as stats]
            [clojure.core.matrix :as m]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))



^{::clerk/viewer clerk/hide-result}
(clerk/add-viewers! [slideshow/viewer])


;; # Example of performing PCA on word and sentence embedding data


(def word-embeddings
  (->> "word-embeddings.edn"
       io/resource
       slurp
       edn/read-string))

(def words ["male" "female" "king" "queen"])


(def pca (stats/principal-components word-embeddings))
(def components (:rotation pca))
(def pc1 (incanter/sel components :cols 0))
(def pc2 (incanter/sel components :cols 1))
(def pc3 (incanter/sel components :cols 2))
(def x (m/mmul word-embeddings pc1))
(def y (m/mmul word-embeddings pc2))
(def z (m/mmul word-embeddings pc3))

(clerk/plotly 
 {:clerk/width :full
  :clerk/height 500}
 {:data [{:x x :y y :z z :type "scatter3d"
          :text words

          :mode "markers+text"
          :textposition "middle right"}]})

