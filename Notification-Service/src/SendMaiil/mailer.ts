import nodemailer from 'nodemailer';
import ejs from 'ejs';
import path from 'path';
import EmailRequest from '../interfaces/EmailRequest';

const sendEmail = async (mailData: EmailRequest) => {
  const { recipient, subject, phone, content } = mailData;

  console.log('Creating transporter...');
  const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'lekhanhuyenn.12@gmail.com',
      pass: 'frbgrgcfqhhzxgva',
    },
  });

  console.log('Reading and rendering EJS template...');
  const templatePath = path.join(__dirname, 'templates', 'emailTemplate.ejs');
  let emailHtml;
  try {
    emailHtml = await ejs.renderFile(templatePath, { recipient, phone, content });
    console.log('EJS template rendered successfully.');
  } catch (error) {
    console.error('Error rendering EJS template:', error);
    throw error; // Re-throw to be caught by the outer catch block
  }

  console.log('Configuring mail options...');
  const mailOptions = {
    from: 'lekhanhuyenn.12@gmail.com',
    to: recipient,
    subject: subject,
    html: emailHtml,
  };

  console.log('Sending email...');
  try {
    await transporter.sendMail(mailOptions);
    console.log('Email sent successfully.');
  } catch (error) {
    console.error('Error sending email:', error);
  }
};

export default sendEmail;
