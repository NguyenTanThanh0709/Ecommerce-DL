"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const hello_1 = __importDefault(require("./hello"));
const router = express_1.default.Router();
router.get("/", (req, res) => {
    res.json({
        message: "Root API1",
    });
});
router.use("/hello", hello_1.default);
exports.default = router;
//# sourceMappingURL=index.js.map