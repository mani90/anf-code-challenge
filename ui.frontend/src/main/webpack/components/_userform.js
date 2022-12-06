// Example of how a component should be initialized via JavaScript
// This script logs the value of the component's text property model message to the console

(function() {
    "use strict";

    // Best practice:
    // For a good separation of concerns, don't rely on the DOM structure or CSS selectors,
    // but use dedicated data attributes to identify all elements that the script needs to
    // interact with.
    var selectors = {
        self:       '[id="new_form"]',
        firstName:  '[name="fname"]',
        lastName:   '[name="lName"]',
        age:        '[name="age"]',
        country:    '[class="country__cmp"]',
        buttom :    '[type="SUBMIT"]',
        hidden:     '[name=":formstart"]'
    };

    function SaveUserDetails(config) {

        

        function init(config) {
            var userFrom = config.element.querySelector(selectors.self);
            var firstName = config.element.querySelector(selectors.firstName);
            var lastName = config.element.querySelector(selectors.lastName);
            var age = config.element.querySelector(selectors.age);
            var country = config.element.querySelector(selectors.country);
            var submitButton = config.element.querySelectorAll(selectors.buttom);
            var hidden = config.element.querySelector(selectors.hidden);


            function validateForm() {
                var status = true;
                if(firstName.value == undefined || firstName.value == "") {
                    firstName.style.border = "3px solid red"
                    status = false;
                }
                if(lastName.value == undefined || lastName.value == "") {
                    lastName.style.border = "3px solid red"
                    status = false;
                }
                if(age.value == undefined || age.value == "") {
                    age.style.border = "3px solid red"
                    status = false;
                }

                return status;
            }


            function callback(response) {
                console.log(response)
                response = JSON.parse(response)
                var message = response.message;

                var container = document.createElement("div");
                var p = document.createElement("p");
                p.style.color = "red";
                p.textContent = message;
                container.appendChild(p);
            

                hidden.before(container);
            }


            function submitForm(event) {
                event.preventDefault();

                if(validateForm()) {
                    var data = "firstName="+ firstName.value + "&lastName="+lastName.value +"&age="+age.value +"&country="+country.innerHTML.split(":")[1].trim();

                    console.log(data);
                    
                    var httpReq = new XMLHttpRequest();
                 
                    httpReq.open("GET", "/bin/saveUserDetails?"+data, true);
                    httpReq.send(null);
                       
                    httpReq.onreadystatechange = () => {
                        console.log(httpReq.readyState)
                        if (httpReq.readyState === 4) {
                          callback(httpReq.response);
                        }
                    }
                }
            }

            function digit(event) {
                console.log(event);
                var fieldVal = document.getElementById(event.target.id).value;
                if(!fieldVal.match(/^[0-9]+$/))
                    event.target.value ="";
            }

            submitButton.forEach(button => 
                button.addEventListener('click', submitForm)
            );

            age.addEventListener("keyup", digit)
            
        }


        if (config && config.element) {
            init(config);
        }
    }

    // Best practice:
    // Use a method like this mutation obeserver to also properly initialize the component
    // when an author drops it onto the page or modified it with the dialog.
    function onDocumentReady() {
        var elements = document.querySelectorAll(selectors.self);
        for (var i = 0; i < elements.length; i++) {
            new SaveUserDetails({ element: elements[i] });
        }

        var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
        var body             = document.querySelector("body");
        var observer         = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                // needed for IE
                var nodesArray = [].slice.call(mutation.addedNodes);
                if (nodesArray.length > 0) {
                    nodesArray.forEach(function(addedNode) {
                        if (addedNode.querySelectorAll) {
                            var elementsArray = [].slice.call(addedNode.querySelectorAll(selectors.self));
                            elementsArray.forEach(function(element) {
                                new SaveUserDetails({ element: element });
                            });
                        }
                    });
                }
            });
        });

        observer.observe(body, {
            subtree: true,
            childList: true,
            characterData: true
        });
    }

    if (document.readyState !== "loading") {
        onDocumentReady();
    } else {
        document.addEventListener("DOMContentLoaded", onDocumentReady);
    }

}());