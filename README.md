<!-- Plugin description -->
# 🧠 Ollama Completion Plugin 🧠

AI-powered inline code completion for IntelliJ IDEA using a local [Ollama](https://ollama.com) LLM model.

This plugin generates smart Java code suggestions directly in your editor using the IntelliJ Inline Completion API and a locally running model like `codellama`, `deepseek-coder`, or any other supported by Ollama.

---

## 🚀 Features

- ✨ Inline gray-text completions while typing
- ⚡ Works fully offline (no external API calls)
- 🧠 AI-based completions from Ollama
- 🧾 Efficient caching mechanism to avoid duplicate requests

---
## 📋 Caching Strategy 

To improve performance and reduce redundant model queries, a windowed cache is used:
-	Caches full prefix → suggestion mappings
-	Stores partial prefix/postfix variants (based on a token window); see examples below
-	Evicts old entries using an LRU strategy

---

## 🔧 How to run plugin in your real IDE

- Install [Ollama](https://ollama.com) and make sure the application is running:
- Download and run the model in your terminal:
~~~ bash
  ollama pull deepseek-coder:6.7b
~~~

- Download this [file](https://github.com/AndreRab/OllamaCompletionPlugin/blob/main/plugin/OllamaCompletionPlugin--0.0.1.zip)
- In your IDE, go to Settings → Plugins → ⚙️ → Install Plugin from Disk…, and select downloaded file:
  [OllamaCompletionPlugin--0.0.1.zip](https://github.com/AndreRab/OllamaCompletionPlugin/blob/main/plugin/OllamaCompletionPlugin--0.0.1.zip) 
- ⭕️ Make sure Ollama is running in the background, open your Java project, and enjoy coding with your new AI assistant!

<!-- Plugin description end -->
---

## 🔧 How to run plugin in a sandbox

- Install [Ollama](https://ollama.com) and run the application:
- Download and run the model in your terminal:
~~~ bash
  ollama pull deepseek-coder:6.7b
~~~
- Clone and run the plugin:
~~~ bash
  git clone https://github.com/your-name/OllamaCompletionPlugin.git
  cd OllamaCompletionPlugin
  ./gradlew runIde
~~~ 
This launches a sandbox instance of IntelliJ with the plugin enabled.

---
## 😎 Usage example
![Projekt bez nazwy](https://github.com/user-attachments/assets/76d21ba3-9cb8-431a-a3c6-a2a604de1096)

