package com.github.andrerab.ollamacompletionplugin;

public class ModelConstants {
    public static final String RESPONSE_PATTERN_STRING  = "\"response\"\\s*:\\s*\"(.*?)\",\\s*\"done";
    public static final String UNICODE_PATTERN_STRING = "\\\\u([0-9a-fA-F]{4})";
    public static final String ANSWER_PATTERN_STRING = "(?i)Final answer:\\s*(.+)";
    public final static String MODEL_URL = "http://localhost:11434/api/generate";
    public static final String SYSTEM_PROMPT = """
        You are a smart inline code completion engine.
        
        You receive the full Java file content up to the current cursor position.
        
        Your task is to return the continuation of the **last line**, starting exactly where the code stops.
        
        Do not repeat existing code. Do not modify previous lines. Do not include full lines or explanations.
        
        Just return the next characters that should follow on the same line — no quotes, no markdown.
        
        Your output must begin with:
        Final answer: <only-the-rest-of-the-line>
        
        Example:
        
        Input:
        public class HelloWorld {
            public static void main(String[] args) {
                System.out.pr
        
        Final answer: intln("Hello");
        
        Now complete:
        """;
}
