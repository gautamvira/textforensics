(ns textforensics.core
  (:use clojure.pprint)
  (:use opennlp.nlp)
  (:use opennlp.tools.filters)
  (:use opennlp.treebank)
  (:require [clojure.string :as str])
  (:gen-class))

(def get-sentences (make-sentence-detector "resources/en-sent.bin"))
(def name-finder (make-name-finder "resources/en-ner-person.bin"))
(def date-finder (make-name-finder "resources/en-ner-date.bin"))
(def location-finder (make-name-finder "resources/en-ner-location.bin"))
(def time-finder (make-name-finder "resources/en-ner-time.bin"))
(def money-finder (make-name-finder "resources/en-ner-money.bin"))
(def percent-finder (make-name-finder "resources/en-ner-percentage.bin"))
(def organization-finder (make-name-finder "resources/en-ner-organization.bin"))
(def tokenize (make-tokenizer "resources/en-token.bin"))
(def pos-tag (make-pos-tagger "resources/en-pos-maxent.bin"))
(def chunker (make-treebank-chunker "resources/en-chunker.bin"))

(def text                                                   ;defining a sample test for the program
  "John Wick is a student at Lakehead University in Thunder Bay.
  John Wick spent an amount of $500 on November 15 in the evening.
  He spent 20% of his budget to gather supplies.")

(def determiners                                            ;determiners as stop words for nlp
  "a about above after again against all am an and any are aren't as at be because been before being below between both but by can't cannot could couldn't did didn't do does doesn't doing don't down during each few for from further had hadn't has hasn't have haven't having he he'd he'll he's her here here's hers herself him himself his how how's i i'd i'll i'm i've if in into is isn't it it's its itself let's me more most mustn't my myself no nor not of off on once only or other ought our ours\tourselves out over own same shan't she she'd she'll she's should shouldn't so some such than that that's the their theirs them themselves then there there's these they they'd they'll they're they've this those through to too under until up very was wasn't we we'd we'll we're we've were weren't what what's when when's where where's which while who who's whom why why's with won't would wouldn't you you'd you'll you're you've your yours yourself yourselves")
(def text2                                                  ;example text by the zodiac killer
"I LIKE KILLING PEOPLE BECAUSE IT IS SO MUCH FUN IT IS MORE FUN THAN KILLING WILD GAME IN THE FORREST BECAUSE MAN IS THE MOST DANGEROUE ANAMAL OF ALL TO KILL SOMETHING GIVES ME THE MOST THRILLING EXPERENCE IT IS EVEN BETTER THAN GETTING YOUR ROCKS OFF WITH A GIRL THE BEST PART OF IT IS THAE WHEN I DIE I WILL BE REBORN IN PARADICE AND ALL THEI HAVE KILLED WILL BECOME MY SLAVES I WILL NOT GIVE YOU MY NAME BECAUSE YOU WILL TRY TO SLOI DOWN OR ATOP MY COLLECTIOG OF SLAVES FOR MY AFTERLIFE. EBEORIETEMETHHPITI")

(defn getName [text]                                        ;function to get the names mentioned in a text
  (name-finder (tokenize text)))

(defn getDate [text]                                        ;function to get the date mentioned in a text
  (date-finder (tokenize text)))

(defn getLocation [text]                                    ;function to get the location mentioned in a text
  (location-finder (tokenize text)))

(defn getTime [text]                                        ;function to get the time mentioned in a text
  (time-finder (tokenize text)))

(defn getOrganization [text]                                ;function to get the organization mentioned in a text
  (organization-finder (tokenize text)))

(defn getAmount [text]                                      ;function to get the amount $ mentioned in a text
  (money-finder (tokenize text)))

(defn getPercent [text]                                     ;function to get any percentage mentioned in a text
  (percent-finder (tokenize text)))

(defn getNouns [text]                                       ;function to get a list of nouns used in a text
   (chunker (nouns (pos-tag (tokenize text)))))

(defn getVerbs [text]                                       ;function to get a list of verbs used in a text
  (chunker (verbs (pos-tag (tokenize text)))))

(defn most-frequent-n [n texts]                             ;function to find the frequency of n words
  (take n (reverse (sort-by val (frequencies texts)))))

(defn frequencywords [count text]                           ;function to return the frequency of word after removing determiners
  (most-frequent-n count (remove (set (str/split determiners #" ")) text)))

(defn -main [& args]                                        ;testing the functions for a sample text
  (println "Name: " (getName text))
  (println "Date: " (getDate text))
  (println "City: " (getLocation text))
  (println "Time: " (getTime text))
  (println "Organization: " (getOrganization text))
  (println "Amount: " (getAmount text))
  (println "Percentage spent on supplies: " (getPercent text))
  (println "Sentences: ")
  (pprint (get-sentences text))
  (println "Nouns: " (getNouns (str/lower-case text2)))
  (println "Verbs: " (getVerbs (str/lower-case text2)))
  (println "Most frequent words: " (frequencywords 5 (str/split (str/lower-case text2) #" ")))
  )

