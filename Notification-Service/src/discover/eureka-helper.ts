const Eureka = require('eureka-js-client').Eureka;

const eurekaHost = 'localhost';
const eurekaPort = 8761;
const hostName =  'localhost';
const ipAddr = '172.0.0.1';

export function registerWithEureka(appName: string, PORT: number): void {
  const client = new Eureka({
    instance: {
      app: appName,
      hostName: hostName,
      ipAddr: ipAddr,
      port: {
        '$': PORT,
        '@enabled': 'true',
      },
      vipAddress: appName,
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: eurekaHost,
      port: eurekaPort,
      servicePath: '/eureka/apps/',
      maxRetries: 10,
      requestRetryDelay: 2000,
    },
  });

  client.logger.level('debug');

  client.start((error: Error) => {
    console.log(error || "Noti service registered");
  });

  function exitHandler(options: { cleanup?: boolean; exit?: boolean }, exitCode: number): void {
    if (options.cleanup) {
      // Perform cleanup tasks here if needed
    }
    if (exitCode !== undefined) {
      console.log(exitCode);
    }
    if (options.exit) {
      client.stop(() => {
        process.exit();
      });
    }
  }

  client.on('deregistered', () => {
    console.log('after deregistered');
    process.exit();
  });

  client.on('started', () => {
    console.log("eureka host  " + eurekaHost);
  });

  process.on('SIGINT', exitHandler.bind(null, { exit: true }));
}
