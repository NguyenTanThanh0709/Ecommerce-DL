import express from 'express';
import MessageResponse from '../interfaces/MessageResponse';
import axios, { AxiosRequestConfig } from 'axios';
const crypto = require('crypto');
import config from './config';

import { sendUpdateStatus, sendCreatePayment } from '../kafka/producer';
interface Config {
    accessKey: string;
    secretKey: string;
    orderInfo: string;
    partnerCode: string;
    redirectUrl: string;
    ipnUrl: string;
    requestType: string;
    extraData: string;
    orderGroupId: string;
    autoCapture: boolean;
    lang: string;
  }

const router = express.Router();

router.post<any>('/payment/', async (req, res) => { // Remove typeof
const { id, totalMoney } = req.body;
    const {
        accessKey,
        secretKey,
        orderInfo,
        partnerCode,
        redirectUrl,
        ipnUrl,
        requestType,
        extraData,
        orderGroupId,
        autoCapture,
        lang,
      }: Config = config;

      const amount = totalMoney.toString();
      const orderId = partnerCode + new Date().getTime();
      const requestId = orderId;

      const rawSignature = `accessKey=${accessKey}&amount=${amount}&extraData=${extraData}&ipnUrl=${ipnUrl}&orderId=${orderId}&orderInfo=${id}&partnerCode=${partnerCode}&redirectUrl=${redirectUrl}&requestId=${requestId}&requestType=${requestType}`;


      //signature
  var signature = crypto
  .createHmac('sha256', secretKey)
  .update(rawSignature)
  .digest('hex');

  const requestBody = JSON.stringify({
    partnerCode: partnerCode,
    partnerName: 'Test',
    storeId: 'MomoTestStore',
    requestId: requestId,
    amount: amount,
    orderId: orderId,
    orderInfo: id,
    redirectUrl: redirectUrl,
    ipnUrl: ipnUrl,
    lang: lang,
    requestType: requestType,
    autoCapture: autoCapture,
    extraData: extraData,
    orderGroupId: orderGroupId,
    signature: signature,
  });

  const options: AxiosRequestConfig = {
    method: 'POST',
    url: 'https://test-payment.momo.vn/v2/gateway/api/create',
    headers: {
      'Content-Type': 'application/json',
      'Content-Length': Buffer.byteLength(requestBody),
    },
    data: requestBody,
  };

  try {
    const result = await axios(options);
    return res.status(200).json(result.data);
  } catch (error) {
    return res.status(500).json({ statusCode: 500, message: (error as Error).message });
  }
});

router.post<any>('/callback/', async (req, res) => {
  console.log('callback: ');
  const {
    partnerCode,
    orderId,
    requestId,
    amount,
    orderInfo,
    orderType,
    transId,
    resultCode,
    message,
    payType,
    responseTime,
    extraData,
    signature,
  } = req.body;

  console.log('Received Callback Data:', req.body);
  if(resultCode == 0){
    // thành công
    // tạo payment
    await sendCreatePayment(orderInfo);
  }else{
    // không thành công update status order cancelled
    await sendUpdateStatus(orderInfo);
  }
  return res.status(204).json("0k");
});


router.post<any>('/check-status-transaction/', async (req, res) => {
  const { orderId } = req.body;

  const secretKey = 'K951B6PE1waDMi640xX08PD3vg6EkVlz';
  const accessKey = 'F8BBA842ECF85';
  const rawSignature = `accessKey=${accessKey}&orderId=${orderId}&partnerCode=MOMO&requestId=${orderId}`;

  const signature = crypto
    .createHmac('sha256', secretKey)
    .update(rawSignature)
    .digest('hex');

  const requestBody = JSON.stringify({
    partnerCode: 'MOMO',
    requestId: orderId,
    orderId: orderId,
    signature: signature,
    lang: 'vi',
  });

  const options: AxiosRequestConfig = {
    method: 'POST',
    url: 'https://test-payment.momo.vn/v2/gateway/api/query',
    headers: {
      'Content-Type': 'application/json',
    },
    data: requestBody,
  };

  try {
    const result = await axios(options);
    return res.status(200).json(result.data);
  } catch (error) {
    return res.status(500).json({ statusCode: 500, message: (error as Error).message });
  }

});
export default router;
