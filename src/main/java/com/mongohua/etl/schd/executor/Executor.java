package com.mongohua.etl.schd.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 执行shell 脚本的类
 * @author xiaohf
 */
public class Executor {
    private Process process;
    protected String errorResult;
    protected String stdoutResult;

    public void exec(String[] cmd, String[] envp) throws IOException,InterruptedException {
        process = Runtime.getRuntime().exec(cmd,envp);
        StreamCaptureThread errorStream = new StreamCaptureThread(process.getErrorStream());
        StreamCaptureThread outputStream = new StreamCaptureThread(process.getInputStream());
        new Thread(errorStream).start();
        new Thread(outputStream).start();
        process.waitFor();

        this.errorResult = errorStream.output.toString();
        this.stdoutResult = outputStream.output.toString();
    }

    public String getErrorResult() {
        return errorResult;
    }

    public String getStdoutResult() {
        return stdoutResult;
    }

    public int getCode() {
        return process.exitValue();
    };

    class StreamCaptureThread implements Runnable {
        InputStream stream;
        StringBuilder output;

        public StreamCaptureThread(InputStream stream) {
            this.stream = stream;
            this.output = new StringBuilder();
        }

        @Override
        public void run() {
            try {
                try {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(this.stream));
                    String line = br.readLine();
                    while (line != null) {
                        if (line.trim().length() > 0) {
                            output.append(line).append("\r\n");
                        }
                        line = br.readLine();
                    }
                } finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}


