# IntelliLangHub
IntelliLangHub is a repository for IntelliJ IDEA IntelliLang plugin [language injections](https://www.jetbrains.com/help/idea/using-language-injections.html). Its purpose is the simplification of sharing language injection rules between users.

IntelliLangHub consists of two modules: server and client.


## Server
IntelliLangHub server is a service that accepts, validates, stores and provides language injection per user requests.


## Client
IntelliLangHub client is an IntelliJ IDEA plugin that pull and commit injections to the server and interacts with IntelliLang plugin to highlight language injections in IDE editor.