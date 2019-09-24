package com.redphase.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

import static com.redphase.framework.util.webview.Java2JavascriptUtils.callHandler;

@Slf4j
public class ViewFactory {
    public static Parent createWebView(String url, Map<String, Object> args) {
        WebView webView = new WebView();
        webView.setContextMenuEnabled(false);
        WebEngine webEngine = webView.getEngine();
        webEngine.setUserDataDirectory(new File("cookie"));

        callHandlerWrapper(webEngine, args);

        // 显示“alert”JavaScript消息输出（用于调试）
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> arg0) {
                log.debug("alertwb1: " + arg0.getData());
            }
        });
        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(new com.sun.javafx.webkit.WebConsoleListener() {
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                log.debug("Console: " + message + " [" + sourceId + ":" + lineNumber + "] ");
            }
        });
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                log.debug("getLocation:" + webEngine.getLocation());
                webEngine.setJavaScriptEnabled(true);
            }
        });
        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(new com.sun.javafx.webkit.WebConsoleListener() {
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                log.debug("Console: " + message + " [" + sourceId + ":" + lineNumber + "] ");
            }
        });
        webEngine.userStyleSheetLocationProperty().addListener((col) -> {
            log.debug("userStyleSheetLocationProperty: " + col);
        });
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        webEngine.load(classloader.getResource(url).toExternalForm());
        return webView;
    }

    private static void callHandlerWrapper(WebEngine webEngine, Map<String, Object> args) {
        if (args != null) {
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                callHandler(webEngine, entry.getKey(), entry.getValue());
            }
        }
    }
}
