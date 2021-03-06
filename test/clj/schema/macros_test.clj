(ns schema.macros-test
  (:use clojure.test)
  (:require
   [schema.core :as s]
   [schema.macros :as macros]))

(deftest normalized-defn-args-test
  (doseq [explicit-meta [{} {:a -1 :c 3}]
          [schema-attrs schema-forms] {{:schema `s/Any} []
                                       {:schema 'Long :tag 'Long} [:- 'Long]}
          [doc-attrs doc-forms] {{} []
                                 {:doc "docstring"} ["docstring"]}
          [attr-map attr-forms] {{} {}
                                 {:a 1 :b 2} [{:a 1 :b 2}]}]
    (let [simple-body ['[x] `(+ 1 1)]
          full-args (concat [(with-meta 'abc explicit-meta)] schema-forms doc-forms attr-forms simple-body)
          [name & more] (macros/normalized-defn-args {} full-args)]
      (testing (vec full-args)
        (is (= (concat ['abc (merge explicit-meta schema-attrs doc-attrs attr-map) simple-body])
               (concat [name (meta name) more])))))))
