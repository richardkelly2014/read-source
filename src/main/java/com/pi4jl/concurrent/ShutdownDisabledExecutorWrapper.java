package com.pi4jl.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by jiangfei on 2019/11/16.
 */
public class ShutdownDisabledExecutorWrapper implements ExecutorService {

    private ExecutorService executorService;

    public ShutdownDisabledExecutorWrapper(ExecutorService executorService) {

        this.executorService = executorService;
    }

    @Override
    public void shutdown() {

        throw new UnsupportedOperationException("This scheduled executor service can only be shutdown by Pi4J.");
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException("This scheduled executor service can only be shutdown by Pi4J.");
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executorService.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executorService.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executorService.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(command);
    }
}
