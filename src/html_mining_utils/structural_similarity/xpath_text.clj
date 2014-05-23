(ns html-mining-utils.structural-similarity.xpath-text
  "Compute XPaths to text and use cosine similarity
   as a metric"
  (:require [structural-similarity.utils :as utils]
            [clojure.string :as string]
            [html-mining-utils.core :as core])
  (:use [clj-xpath.core :only [$x:node+]]))

(def *sim-thresh* 0.58)

(defn char-frequency-representation
  "Provide a set of xpath and text pairs,
   this representation returns (xpath, char) pairs"
  [text-xpaths-coll]
  (reduce
   (fn [acc [x text]]
     (merge-with +' acc {x (count text)}))
   {}
   text-xpaths-coll))

(defn similarity-cosine-char-freq
  [doc1 doc2]
  (let [r1 (char-frequency-representation
            (core/page-text-xpaths doc1))
        r2 (char-frequency-representation
            (core/page-text-xpaths doc2))]
    (core/cosine-similarity r1 r2)))

(defn similar?
  [doc1 doc2]
  (try (<= *sim-thresh*
           (similarity-cosine-char-freq doc1
                                        doc2))
       (catch org.w3c.dom.DOMException e false)))
