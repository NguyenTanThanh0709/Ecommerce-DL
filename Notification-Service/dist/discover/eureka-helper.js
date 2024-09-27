"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.registerWithEureka = registerWithEureka;
const Eureka = require('eureka-js-client').Eureka;
const eurekaHost = 'discovery-server';
const eurekaPort = 8761;
const hostName = 'notification-service';
const ipAddr = '172.0.0.1';
function registerWithEureka(appName, PORT) {
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
    client.start((error) => {
        console.log(error || "Noti service registered");
    });
    function exitHandler(options, exitCode) {
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
//# sourceMappingURL=eureka-helper.js.map