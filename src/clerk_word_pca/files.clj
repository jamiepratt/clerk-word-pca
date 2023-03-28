(ns clerk-word-pca.files
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]))


(defn get-file [filename]
  (-> filename
      io/resource
      slurp))

(defn get-lines-from-file [filename]
  (-> filename
      get-file
      str/split-lines
      vec))

(defn get-edn-from-file [filename]
  (-> filename
      get-file
      edn/read-string))