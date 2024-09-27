"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const app_1 = __importDefault(require("./app"));
require("dotenv").config();
const eureka_helper_1 = require("./discover/eureka-helper");
const port = 8085;
app_1.default.listen(port, () => {
    console.log(`server is listening on ${port}`);
});
(0, eureka_helper_1.registerWithEureka)('notification-service', port);
//# sourceMappingURL=index.js.map