^{:nextjournal.clerk/visibility #{:hide-ns :hide}}
(ns ^:nextjournal.clerk/no-cache iris
  (:require [nextjournal.clerk :as clerk]
            [nextjournal.clerk-slideshow :as slideshow]
            [incanter.core :as incanter]
            [incanter.stats :as stats]
            [clojure.core.matrix :as m]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))



^{::clerk/viewer clerk/hide-result}
(clerk/add-viewers! [slideshow/viewer])


;; # Example of performing PCA on Fisher's iris data


(defn parse-cell [cell]
  (->>
   cell
   (#(if (= (first %) \.) (str "0" %) %))
   edn/read-string))

(def iris
  (->> "iris.csv"
       io/resource
       slurp
       csv/read-csv
       (mapv (partial mapv parse-cell))))


(clerk/table iris)
iris

(def iris-dataset (incanter/dataset (first iris) (rest iris)))

(m/shape iris-dataset)
(def X (incanter/sel iris-dataset :cols (range 4)))
(def species (incanter/sel iris-dataset :cols 4))
(def pca (stats/principal-components X))
(def components (:rotation pca))
(def pc1 (incanter/sel components :cols 0))
(def pc2 (incanter/sel components :cols 1))
(def pc3 (incanter/sel components :cols 2))
(def x (m/mmul X pc1))
(def y (m/mmul X pc2))
(def z (m/mmul X pc3))


(interleave (repeat :text) species)

(reduce #(assoc %1 :text %2) {} species)

(clerk/plotly 
 {:clerk/width :full
  :clerk/height 500}
 {:data [{:x x :y y :z z :type "scatter3d" :mode "markers"
          :text species}]})

