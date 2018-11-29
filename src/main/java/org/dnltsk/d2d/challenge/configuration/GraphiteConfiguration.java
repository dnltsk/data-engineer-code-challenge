package org.dnltsk.d2d.challenge.configuration;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteReporter.Builder;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
@ConditionalOnProperty(
    name = "graphite.enabled",
    havingValue=  "true"
)
public class GraphiteConfiguration extends MetricsConfigurerAdapter {
 
  @Value("${graphite.host}")
  private String graphiteHost;
 
  @Value("${graphite.port}")
  private int graphitePort;
 
  @Value("${graphite.refreshPeriodInSeconds}")
  private long refreshPeriodInSeconds;

  @Value("${graphite.prefix}")
  private String graphitePrefix;

  @Autowired
  private MetricRegistry registry;

  @PostConstruct()
  public void init() {
    configureReporters(registry);
  }

  @Override
  public void configureReporters(MetricRegistry metricRegistry) {
    registerReporter(JmxReporter.forRegistry(metricRegistry)
      .build()).start();
    GraphiteReporter graphiteReporter = getGraphiteReporterBuilder(metricRegistry)
      .build(getGraphite());
    registerReporter(graphiteReporter);
    graphiteReporter.start(refreshPeriodInSeconds,
      TimeUnit.SECONDS);
  }
 
  private Builder getGraphiteReporterBuilder(MetricRegistry
    metricRegistry) {
    metricRegistry.register("gc", new GarbageCollectorMetricSet());
    metricRegistry.register("memory", new MemoryUsageGaugeSet());
    metricRegistry.register("threads", new ThreadStatesGaugeSet());
    return GraphiteReporter.forRegistry(metricRegistry)
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .filter(MetricFilter.ALL)
      .prefixedWith(graphitePrefix);
  }
 
  private Graphite getGraphite() {
    return new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
  }
}