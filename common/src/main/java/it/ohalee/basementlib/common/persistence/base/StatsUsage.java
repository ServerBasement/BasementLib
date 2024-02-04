package it.ohalee.basementlib.common.persistence.base;

import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;

import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;

public class StatsUsage {

    public StatsUsage(AbstractBasementPlugin plugin) {
        try {
            SparkProvider.get();
        } catch (Exception e) {
            plugin.logger().severe("Spark is not available, disabling stats usage");
        }
    }

    public static double mspt() {
        try {
            return SparkProvider.get().mspt().poll(StatisticWindow.MillisPerTick.SECONDS_10).mean();
        } catch (Exception e) {
            return 0;
        }
    }

    public static double cpu() {
        try {
            return SparkProvider.get().cpuProcess().poll(StatisticWindow.CpuUsage.SECONDS_10);
        } catch (Exception e) {
            return 0;
        }
    }

    // in MiB
    public static double ram() {
        try {
            return ((double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())) / (1024 * 1024);
        } catch (Exception e) {
            return 0;
        }
    }
}
