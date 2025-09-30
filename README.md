# File Hashing Lab with AWS S3

## Objectives of this Lab

This lab demonstrates the fundamental concepts of file hashing using SHA-256 and its practical application in ensuring data integrity. It also integrates with AWS S3 for secure storage and verification of files and their corresponding hashes.

Specifically, this lab aims to:
1. **Generate SHA-256 hashes** for files to understand cryptographic hashing
2. **Illustrate the "avalanche effect"** of hashing algorithms by showing how a minor change in a file results in a drastically different hash
3. **Detect file modifications** using hash comparison, highlighting the importance of hashing for data integrity verification
4. **Utilize AWS S3** to store original files, modified files, and their respective hash values, demonstrating a real-world scenario for secure data management
5. **Understand AWS permissions** and how to configure proper access for S3 operations

## Commands Used

Here are the key commands and configurations used during this lab:

### 1. AWS CLI Configuration

Before running the application with S3 upload, ensure your AWS CLI is configured:

```bash
aws configure
```
*(This command prompts you to enter your AWS Access Key ID, Secret Access Key, default region, and default output format.)*

### 2. Creating an S3 Bucket

Create an S3 bucket for storing the files:

```bash
aws s3 mb s3://hashing-lab-wiame-2025 --region us-east-1
```
*(Replace `hashing-lab-wiame-2025` with your desired bucket name and `us-east-1` with your chosen region.)*

### 3. Running the Application

To run the File Hashing Lab application:

```bash
cd "C:\Users\Wiame\Desktop\S5\TP - Sécurité des SI\TP-Hashing"
mvn compile exec:java -Dexec.mainClass="com.hashinglab.FileHashingLab"
```
*(This command compiles and runs the Java application using Maven.)*

## Steps and Screenshots

Follow these steps to set up and run the hashing lab with AWS S3 integration:

### Step 1: Project Setup

1. **Clone the repository** (or set up the project in your IDE)
2. **Ensure all dependencies are properly configured** in `pom.xml`

### Step 2: Code Configuration

1. **Examine the project structure** to understand the codebase
2. **Configure the file path** for `original_file.txt` in `FileHashingLab.java`
<img width="607" height="158" alt="Capture d'écran 2025-09-30 143820" src="https://github.com/user-attachments/assets/f8f51ec9-0710-43ef-83db-c90d074ce461" />

4. **Set up the S3 bucket name** in the code (line 110)
<img width="753" height="158" alt="Capture d'écran 2025-09-30 143807" src="https://github.com/user-attachments/assets/bd6c381b-4474-40da-9108-58153c61188a" />    

### Step 3: AWS S3 Configuration

1. **Configure AWS CLI** using `aws configure` if you haven't already
<img width="896" height="182" alt="Capture d'écran 2025-09-30 134219" src="https://github.com/user-attachments/assets/a10f873e-8651-4e5f-9905-588089f683d2" />

3. **Create an S3 bucket** in your AWS account (e.g., `hashing-lab-wiame-2025` in `us-east-1`)
<img width="1042" height="406" alt="Capture d'écran 2025-09-30 135634" src="https://github.com/user-attachments/assets/74e02321-802c-4861-b6f7-2999093bfbcf" />

### Step 4: Enable S3 Upload in Code

1. **Update the `bucketName` variable** (around line 110) with your actual S3 bucket name:
    ```java
    String bucketName = "hashing-lab-wiame-2025"; // Replace with your actual bucket name
    ```
2. **Ensure S3 upload is enabled** for both original and modified files

### Step 5: Run the Application and Verify S3 Upload

1. **Run the application** using the Maven command
2. **Observe the console output** for hash generation, file modification, and S3 upload messages
<img width="734" height="107" alt="Capture d'écran 2025-09-30 144546" src="https://github.com/user-attachments/assets/e68da4b4-e8d2-4beb-9e9a-cfba06b53f43" />

4. **Check your S3 bucket** in the AWS S3 console
<img width="1563" height="500" alt="Capture d'écran 2025-09-30 135644" src="https://github.com/user-attachments/assets/e5b3a28a-c2d4-421c-b3d6-61714ace6fbb" />

### Step 6: Verify Uploaded Files

1. **Navigate to the `original/` folder** in your S3 bucket to see the original file and its hash
<img width="1567" height="505" alt="Capture d'écran 2025-09-30 135711" src="https://github.com/user-attachments/assets/cf802cc8-dc7b-4eb3-abd6-7dd90f40091d" />

3. **Navigate to the `modified/` folder** to see the modified file and its hash
<img width="1558" height="544" alt="Capture d'écran 2025-09-30 135656" src="https://github.com/user-attachments/assets/8a110ead-8e17-4404-81d7-aaa9d831cb9f" />

## Expected Results

After successful execution, you should see:

### Console Output
- Hash generation for the original file
- File modification (adding a space)
- Hash comparison showing the avalanche effect
- S3 upload confirmations

### S3 Bucket Structure
```
hashing-lab-wiame-2025/
├── original/
│   ├── original_file.txt
│   └── file_hash.txt
└── modified/
    ├── modified_file.txt
    └── modified_file_hash.txt
```

### Hash Comparison Results
- **Original Hash**: `d800a7ea3b1567a1d9481cb238f71a79f940deb38e18be59bb41c45e15ff63ac`
- **Modified Hash**: `51960e612d8d2beca1e30a7a237ddb48f59d7443f5c291d8ca2cf980b493ea13`
- **Differences**: 62/64 characters different (demonstrating avalanche effect)

## Technologies Used

- **Java 17** - Programming language
- **Maven** - Build tool and dependency management
- **AWS SDK for Java** - AWS service integration
- **SHA-256** - Cryptographic hashing algorithm
- **AWS S3** - Cloud storage service
- **AWS CLI** - Command-line interface for AWS
- **IntelliJ IDEA** - Development environment

## Conclusion

This lab successfully demonstrates the practical application of cryptographic hashing for data integrity verification, combined with cloud storage using AWS S3. The avalanche effect of SHA-256 is clearly illustrated, showing how even minimal file changes result in completely different hash values, making it an excellent tool for detecting data tampering or corruption.

---

Wiame Yousfi - Security Labs
