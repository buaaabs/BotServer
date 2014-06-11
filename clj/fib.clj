(ns user) (defn foo [a b]   (str a " " b))
(defn fib [n]
	(if (<= n 1)
		1
		(+ (fib (- n 1))
		(fib (- n 2)))))
(fib 10)
(println "Sxf!HoWO!")