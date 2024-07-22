package br.com.sicredi.sincronizacao.infra.file.processor.csv;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
public class CSVThreadPoolConfig {

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    @Value("${csv.config.default.threads.min_pool_size}")
    private String defaultMinNumThreads;

    @Value("${csv.config.default.threads.max_pool_size}")
    private String defaultMaxNumThreads;

    @Value("${csv.config.default.threads.keep_alive_time}")
    private String defaultKeepAliveTime;

    @Getter
    @Value("${csv.config.default.timeout_in_seconds}")
    private String timeoutInSeconds;


    public ThreadPoolExecutor createThreadPoolExecutor(String minNumThreads, String maxNumThreads, String keepAliveTime) {

        String execMinNumThreads = minNumThreads == null || Integer.parseInt(minNumThreads) < 0 ? defaultMinNumThreads : minNumThreads;
        String execMaxNumThreads = maxNumThreads == null || Integer.parseInt(maxNumThreads) < 0 ? defaultMaxNumThreads : maxNumThreads;
        String execKeepAliveTime = keepAliveTime == null || Integer.parseInt(keepAliveTime) < 0 ? defaultKeepAliveTime : keepAliveTime;

        if(execMaxNumThreads == null){
            execMaxNumThreads = String.valueOf(Runtime.getRuntime().availableProcessors());
        }

        if (execMinNumThreads == null || execKeepAliveTime == null) {
            throw new IllegalArgumentException("Não foi possível criar o ThreadPoolExecutor, ExecMinNumThreads e/ou ExecKeepAliveTime são null!");
        }

        return new ThreadPoolExecutor(
                Integer.parseInt(execMinNumThreads),
                Integer.parseInt(execMaxNumThreads),
                Long.parseLong(execKeepAliveTime),
                TIME_UNIT,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

}
