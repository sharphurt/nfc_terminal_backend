package ru.catstack.nfc_terminal.model.enums;

public enum PaymentStatus {
    SUCCESSFULLY {
        @Override
        public String toString() {
            return "The payment was successfully confirmed by the payer's bank";
        }
    },
    DENIED {
        @Override
        public String toString() {
            return "The payment was rejected by the payer's bank";
        }
    },
    WAITING {
        @Override
        public String toString() {
            return "The payment is awaiting confirmation by the payer's bank";
        }
    },
    RETURNED {
        @Override
        public String toString() {
            return "The payment was successfully returned to payer";
        }
    }
}
