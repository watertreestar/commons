package com.young.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RetryOperation<T> {
    private final RetryOperation.RetryConsumer<T> retryConsumer;
    private final int noOfRetry;
    private final int delayInterval;
    private final TimeUnit timeUnit;
    private final RetryOperation.RetryPredicate<T> retryPredicate;
    private final List<Class<? extends Throwable>> exceptionList;

    private RetryOperation(RetryOperation.RetryConsumer<T> retryConsumer, int noOfRetry, int delayInterval, TimeUnit timeUnit,
                           RetryOperation.RetryPredicate<T> retryPredicate, List<Class<? extends Throwable>> exceptionList) {
        this.retryConsumer = retryConsumer;
        this.noOfRetry = noOfRetry;
        this.delayInterval = delayInterval;
        this.timeUnit = timeUnit;
        this.retryPredicate = retryPredicate;
        this.exceptionList = exceptionList;
    }

    public static <T> OperationBuilder<T> newBuilder() {
        return new RetryOperation.OperationBuilder<>();
    }

    public T retry() throws Exception {
        T result = null;
        int retries = 0;

        while (retries < this.noOfRetry) {
            try {
                result = this.retryConsumer.evaluate();
                if (!Objects.nonNull(this.retryPredicate)) {
                    return result;
                }

                boolean shouldItRetry = this.retryPredicate.shouldRetry(result);
                if (!shouldItRetry) {
                    return result;
                }

                retries = this.increaseRetryCountAndSleep(retries);
            } catch (Exception e) {
                retries = this.handleException(retries, e);
            }
        }

        return result;
    }

    private int handleException(int retries, Exception e) throws Exception {
        if (!this.exceptionList.isEmpty() && this.exceptionList.stream().noneMatch((ex) -> ex.isAssignableFrom(e.getClass()))) {
            throw e;
        } else {
            retries = this.increaseRetryCountAndSleep(retries);
            if (retries == this.noOfRetry) {
                throw e;
            } else {
                return retries;
            }
        }
    }

    private int increaseRetryCountAndSleep(int retries) {
        ++retries;
        if (retries < this.noOfRetry && this.delayInterval > 0) {
            try {
                this.timeUnit.sleep(this.delayInterval);
            } catch (InterruptedException var3) {
                Thread.currentThread().interrupt();
            }
        }
        return retries;
    }


    public interface RetryPredicate<T> {
        boolean shouldRetry(T obj);
    }


    public interface RetryConsumer<T> {
        T evaluate() throws Exception;
    }

    public static class OperationBuilder<T> {
        private RetryOperation.RetryConsumer<T> iRetryConsumer;
        private int iNoOfRetry;
        private int iDelayInterval;
        private TimeUnit iTimeUnit;
        private RetryOperation.RetryPredicate<T> iRetryPredicate;
        private Class<? extends Throwable>[] exceptionClasses;
        private String message = "";

        private OperationBuilder() {
        }

        public RetryOperation.OperationBuilder<T> retryConsumer(RetryOperation.RetryConsumer<T> retryConsumer) {
            this.iRetryConsumer = retryConsumer;
            return this;
        }

        public RetryOperation.OperationBuilder<T> noOfRetry(int noOfRetry) {
            this.iNoOfRetry = noOfRetry;
            return this;
        }

        public RetryOperation.OperationBuilder<T> delayInterval(int delayInterval, TimeUnit timeUnit) {
            this.iDelayInterval = delayInterval;
            this.iTimeUnit = timeUnit;
            return this;
        }

        public RetryOperation.OperationBuilder<T> retryPredicate(RetryOperation.RetryPredicate<T> retryPredicate) {
            this.iRetryPredicate = retryPredicate;
            return this;
        }

        @SafeVarargs
        public final RetryOperation.OperationBuilder<T> retryOn(Class<? extends Throwable>... exceptionClasses) {
            this.exceptionClasses = exceptionClasses;
            return this;
        }

        public RetryOperation.OperationBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public RetryOperation<T> build() {
            if (Objects.isNull(this.iRetryConsumer)) {
                throw new RuntimeException("'#retryConsumer:RetryConsumer<T>' not set");
            } else {
                List<Class<? extends Throwable>> exceptionList = new ArrayList();
                if (Objects.nonNull(this.exceptionClasses) && this.exceptionClasses.length > 0) {
                    exceptionList = Arrays.asList(this.exceptionClasses);
                }

                this.iNoOfRetry = this.iNoOfRetry == 0 ? 1 : this.iNoOfRetry;
                this.iTimeUnit = Objects.isNull(this.iTimeUnit) ? TimeUnit.MILLISECONDS : this.iTimeUnit;
                return new RetryOperation<>(this.iRetryConsumer, this.iNoOfRetry, this.iDelayInterval, this.iTimeUnit, this.iRetryPredicate, (List) exceptionList);
            }
        }
    }
}
