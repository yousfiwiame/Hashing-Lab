package com.hashinglab;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class FileHashingLab {

    // Method to generate SHA-256 hash of a file
    public static String generateSHA256Hash(String filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Read the file
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

        // Calculate the hash
        byte[] hashBytes = digest.digest(fileBytes);

        // Convert to hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    // Method to save hash to a file
    public static void saveHashToFile(String hash, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("SHA-256 Hash:\n");
            writer.write(hash);
            writer.write("\n\nGeneration date: " + java.time.LocalDateTime.now());
        }
    }

    // Method to upload to AWS S3
    public static void uploadToS3(String bucketName, String keyName, String filePath) {
        Region region = Region.US_EAST_1; // Modify according to your region
        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3.putObject(putOb, RequestBody.fromFile(new File(filePath)));
            System.out.println("✓ File uploaded successfully to S3: " + keyName);

        } catch (Exception e) {
            System.err.println("Error during S3 upload: " + e.getMessage());
        } finally {
            s3.close();
        }
    }

    // Method to display comparison of two hashes
    public static void compareHashes(String hash1, String hash2, String label1, String label2) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("HASH COMPARISON");
        System.out.println("=".repeat(70));
        System.out.println(label1 + ":");
        System.out.println(hash1);
        System.out.println("\n" + label2 + ":");
        System.out.println(hash2);
        System.out.println("\nAre the hashes identical? " + (hash1.equals(hash2) ? "YES ✓" : "NO ✗"));

        if (!hash1.equals(hash2)) {
            int differences = 0;
            for (int i = 0; i < hash1.length(); i++) {
                if (hash1.charAt(i) != hash2.charAt(i)) {
                    differences++;
                }
            }
            System.out.println("Number of different characters: " + differences + "/" + hash1.length());
        }
        System.out.println("=".repeat(70) + "\n");
    }

    public static void main(String[] args) {
        try {
            // PART 1: Generate hash of the original file
            System.out.println("=== PART 1: ORIGINAL FILE HASHING ===\n");

            String originalFile = "src/main/resources/original_file.txt";
            String hashOutputFile = "file_hash.txt";

            // Generate the hash
            String originalHash = generateSHA256Hash(originalFile);
            System.out.println("✓ Hash generated for the original file:");
            System.out.println(originalHash);

            // Save the hash
            saveHashToFile(originalHash, hashOutputFile);
            System.out.println("✓ Hash saved in: " + hashOutputFile);

            // Upload to S3 - Configure your bucket name here
            String bucketName = "hashing-lab-wiame-2025";
            System.out.println("\n=== UPLOADING TO S3 ===");
            uploadToS3(bucketName, "original/" + hashOutputFile, hashOutputFile);
            uploadToS3(bucketName, "original/" + originalFile, originalFile);

            // PART 2: Modify the file and rehash
            System.out.println("\n=== PART 2: MODIFICATION AND NEW HASH ===\n");

            String modifiedFile = "modified_file.txt";
            String modifiedHashFile = "modified_file_hash.txt";

            // Create a modified version (add a character)
            String content = new String(Files.readAllBytes(Paths.get(originalFile)));
            Files.write(Paths.get(modifiedFile), (content + " ").getBytes());
            System.out.println("✓ Modified file created (added a space)");

            // Generate the new hash
            String modifiedHash = generateSHA256Hash(modifiedFile);
            saveHashToFile(modifiedHash, modifiedHashFile);
            System.out.println("✓ New hash generated and saved");

            // Compare the hashes
            compareHashes(originalHash, modifiedHash,
                    "Original Hash", "Modified Hash");

            // Upload modified files to S3
            System.out.println("\n=== UPLOADING MODIFIED FILES TO S3 ===");
            uploadToS3(bucketName, "modified/" + modifiedHashFile, modifiedHashFile);
            uploadToS3(bucketName, "modified/" + modifiedFile, modifiedFile);

            // CONCLUSION
            System.out.println("=== CONCLUSION ===");
            System.out.println("• A minimal change (1 character) produces a completely different hash");
            System.out.println("• This demonstrates the avalanche effect of SHA-256");
            System.out.println("• Hashing allows detection of any file modification");
            System.out.println("• Essential for data integrity verification");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}