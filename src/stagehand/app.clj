;; file: src/stagehand/app.clj

(ns stagehand.app
  "Server Inventory Management"
   ;; To start working with ring we need a server+adapter
   ;; Jetty is a good default choice
  (:require [ring.adapter.jetty :as jetty]
            [clojure.pprint :refer [pprint]]
            [taoensso.timbre :as timbre
             :refer [log debug info warn error fatal]]))

(info "Logging is working!")

;; Adapters convert between server specifics to more general ring 
;; requests and response maps. This allows you to change out the server
;; without updating any of your handlers.

;; We'll store the reference to the server in an atom for easy
;; starting and stopping
(defonce server (atom nil))

;; Any function that returns a response is a "handler."
;; Responses are just maps! Ring takes care of the rest
(defn hello
  [_request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello World\n"})
;; note: the `_` in `_request` indicates the argument is unused, while
;; still giving it a useful name

;; Returns information regarding the HTTP request
(defn webip [request-map]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (str "Browser information, as detected by the web server:\n"
         "Server's IP address:  " (:server-name request-map) "\n"
         "Server lisenting on port:  " (:server-port request-map) "\n"
         "Client IP address:  " (:remote-addr request-map) "\n"
         "Client request scheme:  " (:scheme request-map) "\n"
         "Client request method:  " (:request-method request-map) "\n"
;         "Client query string:  " (:query-string request-map) "\n"
;         "Client request string:  " (:request-string request-map) "\n"
         "Client request URI:  " (:uri request-map) "\n") })

;; Same as above, the 404 handler just returns a map
;; with the "Not Found" status code
(defn not-found
  [request]
  {:status 404
   :headers {"Content-Type" "text/plain"}
   :body (str "Not Found: " (:uri request) "\n")})

(defn dump [request]
  {:status 200
   :headers {"Content-Type" "test/plain"}
   :body (with-out-str (pprint request))})

;; app is the main handler of the application - it'll get
;; called for every request. It will route the request
;; to the correct function.
;;
;; For routing we'll start by just matching the URI.
;; We'll add in a real routing solution in the next blog post
(defn app
  [request]
  (case (:uri request)
    "/" (hello request)
    "/webip" (webip request)
    "/dump" (dump request)
    ;; Default Handler
    (not-found request)))

;; start! the Jetty web server
(defn start! [opts]
  (reset! server
   (jetty/run-jetty (fn [r] (app r)) opts)))
;; note: the anonymous function used as the handler allows us to revaluate the
;; `app` handler at the REPL to add additional routes / logic without
;; restarting the server or process.
;;
;; Another option is to pass in the handler as a var, `#'app`
;; For a deeper explanation check here:
;; https://clojure.org/guides/repl/enhancing_your_repl_workflow

;; stop! the server and resets the atom back to nil
(defn stop! []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))

;; -main is used as an entry point for the application
;; when running it outside of the REPL.
(defn -main
  "Invoke me with clojure -M -m stagehand.app"
  [& _args]
  (let [port (if (System/getenv "PORT") (Integer. (System/getenv "PORT"))
                  8080)] ; Jetty defaults to port 3000
    (info (str "Server running at: http://localhost:" port))
    (start! {:port port :join? true})))

;; This is a "Rich" comment block. It serves as a bit of documentation and
;; is convenient for use in the REPL. All the code above is available for use,
;; including our handlers!
(comment
  ;; Just call the handler by providing your own request map - no need
  ;; to actually run the server
  (app {:uri "/"})
; {:status 200,
;  :headers {"Content-Type" "text/plain"},
;  :body "Hello World"}

  ;; For use at the REPL - setting :join? to false to prevent Jetty
  ;; from blocking the thread
  (start! {:port 3000 :join? false})

  ;; Evaluate whenever you need to stop
  (stop!)

  ;; At the REPL, *e is bound to the most recent exception
  *e)
