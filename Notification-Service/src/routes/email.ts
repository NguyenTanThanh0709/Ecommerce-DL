import express from 'express';
import MessageResponse from '../interfaces/MessageResponse';
import sendEmail from '../SendMaiil/mailer';
import EmailRequest from '../interfaces/EmailRequest';

const router = express.Router();

router.post<{}, MessageResponse, EmailRequest>('/send', async (req, res) => { // Remove typeof
  const { recipient, subject, phone, content } = req.body;

  if (!recipient || !subject || !phone || !content) {
    return res.status(400).send({ message: 'Missing required fields' });
  }

  try {
    await sendEmail({ recipient, subject, phone, content });
    res.status(200).send({ message: 'Email sent successfully' });
  } catch (error) {
    res.status(500).send({ message: 'Error sending email' });
  }
});

export default router;
