import { Kafka, RecordMetadata } from "kafkajs";

const kafka = new Kafka({
    clientId: "client-test-id",
    brokers: ["localhost:9092"],
  });
  
  const producer = kafka.producer();


  // Hàm gửi dữ liệu đến topic 'update-status'
export const sendUpdateStatus = async (idOrder: string) => {
  try {
    await producer.send({
      topic: "updatestatus",
      messages: [{ value: idOrder }],
    });
    console.log(`Update status message sent for order ID: ${idOrder}`);
  } catch (error) {
    console.error("Error sending update status message:", error);
  }
};


// Hàm gửi dữ liệu đến topic 'create-payment'
export const sendCreatePayment = async (id: string) => {
  try {
    await producer.send({
      topic: "createpayment",
      messages: [{ value: id }],
    });
    console.log(`Create payment message sent for ID: ${id}`);
  } catch (error) {
    console.error("Error sending create payment message:", error);
  }
};

// Hàm khởi động producer
const run = async () => {
  await producer.connect();
  console.log("Producer connected");
};

// Khởi động producer
run().catch(console.error);

// Clean up on exit
process.on("SIGINT", async () => {
  console.log("Disconnecting producer...");
  await producer.disconnect();
  process.exit(0);
});