from flask import Flask, request, jsonify
import json, base64, hashlib, time

app = Flask(__name__)

# -------------------------------
# Replace these with your TEST credentials
# -------------------------------
MERCHANT_ID = "PGTEST123"  # your sandbox merchant ID
SALT_KEY = "your_salt_key" # from PhonePe Sandbox
SALT_INDEX = "1"
API_ENDPOINT = "/pg/v1/pay"

# Create Payment Endpoint
@app.route("/createPayment", methods=["POST"])
def create_payment():
    data = request.json
    amount = data.get("amount", 10000)  # in paise (₹100 = 10000)
    user_id = data.get("userId", "student123")

    body = {
        "merchantId": MERCHANT_ID,
        "merchantTransactionId": str(int(time.time() * 1000)),
        "merchantUserId": user_id,
        "amount": amount,
        "callbackUrl": "https://webhook.site/your-temp-url",  # dummy URL
        "paymentInstrument": {
            "type": "UPI_INTENT",
            "targetApp": "com.phonepe.app.preprod"  # sandbox app
        }
    }

    json_body = json.dumps(body)
    base64_body = base64.b64encode(json_body.encode()).decode()

    checksum = hashlib.sha256((base64_body + API_ENDPOINT + SALT_KEY).encode()).hexdigest()
    checksum = f"{checksum}###{SALT_INDEX}"

    response = {
        "base64Body": base64_body,
        "checksum": checksum,
        "url": API_ENDPOINT,
        "merchantTransactionId": body["merchantTransactionId"]
    }
    return jsonify(response)

# Dummy check status endpoint
@app.route("/checkStatus", methods=["POST"])
def check_status():
    txn_id = request.json.get("merchantTransactionId")
    return jsonify({"status": "SUCCESS", "merchantTransactionId": txn_id})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
