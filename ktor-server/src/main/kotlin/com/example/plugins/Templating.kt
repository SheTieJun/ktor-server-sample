package com.example.plugins

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.html.*
import org.koin.ktor.ext.inject

fun Application.configureTemplating() {
    routing {
        val client by inject<HttpClient>()
        get("/index") {
            val markdownContent: String = client.get("http://localhost:8080/static/page/Page1.MD").bodyAsText()
            val escapedMarkdownContent = markdownContent
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("$", "\\$")
            val script = """
                <script>
                   function openChat(){
                      window.open("http://localhost:8080/static/chat.html")
                   }
                   function callExternalFunction() {
                        fetch('/call-external-function')
                            .then(response => response.text())
                            .then(result => {
                              console.log(result);
                              alert(result);
                            // 处理返回的结果
                            })
                            .catch(error => {
                                console.error(error);
                                // 处理错误
                            });
                    }
                    let md = `$escapedMarkdownContent`
                    document.getElementById('content').innerHTML = marked.parse(md);
                </script>
                """.trimIndent()

            call.respondHtml {
                body {
                    style = "text-align: center;"
                    script {
                        src = "https://cdn.jsdelivr.net/npm/marked/marked.min.js"
                    }
                    div {
                        id = "content"
                    }
                    unsafe {
                        +script
                    }
                    div {
                        a {
                            href = "/static/aboutme.html"
                            +"About me"
                        }
                    }
                    div {
                        onClick = "callExternalFunction()"
                        button {
                            +"Click me"
                        }
                    }
                    div {
                        onClick = "openChat()"
                        button {
                            +"Chat"
                        }
                    }
                    form(action = "/user/login",encType = FormEncType.multipartFormData, method = FormMethod.post) {
                        id = "myForm"
                        p {
                            +"Username:"
                            textInput(name = "userName")
                        }
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        p {
                            submitInput() { value = "Login" }
                        }
                    }
                    unsafe {
                        +"""
                            <script>document.getElementById("myForm").addEventListener("submit", function(event) {
                                event.preventDefault(); // 阻止表单默认提交行为
                            
                                var form = document.getElementById("myForm");
                                var formData = new FormData(form);
                            
                                var jsonData = {};
                                formData.forEach(function(value, key) {
                                    jsonData[key] = value;
                                });
                            
                                var requestOptions = {
                                    method: "POST",
                                    headers: {
                                        "Content-Type": "application/json"
                                    },
                                    body: JSON.stringify(jsonData)
                                };
                            
                                fetch("/user/login", requestOptions)
                                    .then(response => response.json())
                                    .then(data => {
                                        console.log(data);
                                        alert(JSON.stringify(data));
                                        if(data.code == 200){
                                            window.location.href = "/static/aboutme.html";
                                        }
                                        // 处理服务器返回的 JSON 数据
                                    })
                                    .catch(error => {
                                        console.error(error);
                                        // 处理错误
                                    });
                                   });
                          
                                
                            </script>
                        """.trimIndent()
                    }
                }

            }
        }

        get("/call-external-function") {
            call.respond(HttpStatusCode.OK, "Hello from Ktor!")
        }
    }


}


