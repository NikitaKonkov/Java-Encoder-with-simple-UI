# HUMMEL ENCODER

HUMMEL ENCODER is a Java Swing application that provides encryption and decryption functionality using the HUMMEL cipher algorithm.

## Features

- Set the key length for the cipher.
- Encrypt and decrypt text with a password.
- Display the encrypted text and public key.
- Copy the encrypted text and public key to the clipboard.

## Usage

1. Run the `HUMMEL_ENCODER` class.
2. Enter the desired key length in the `PB[1-15] PV[16-1024]` field and click `Keylen`.
3. Enter the text to be encrypted in the `Text` field.
4. Enter the password in the `Password` field.
5. Click `Encrypt` to encrypt the text. The encrypted text and public key will be displayed.
6. Click `Decrypt` to decrypt the encrypted text.
7. Click `Copy Text` to copy the encrypted text to the clipboard.
8. Click `Copy Key` to copy the public key to the clipboard.

## Note

- The `SHUFFLE_CYPHER` class is referenced but not included in the provided code. It should be implemented and included in the project for the encryption and decryption to work.
- The `setPRIMES` method in `SHUFFLE_CYPHER` should be called with a valid signature.

## Running the Application

1. Compile the Java files.
2. Run the `HUMMEL_ENCODER` class.
