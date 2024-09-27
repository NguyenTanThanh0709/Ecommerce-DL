import express from "express";

import MessageResponse from "../interfaces/MessageResponse";
import email from './email';
import momo from './momo'
const router = express.Router();

router.get<{}, MessageResponse>("/", (req, res) => {
  res.json({
    message: "Root API1",
  });
});





router.use('/email', email);
router.use('/', momo);
export default router;