#!/bin/bash

# Spring Boot Auto Deploy Script (Simplified)
exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1
echo "=== Spring Boot Auto Deploy Started at $(date) ==="

# System update
echo "1. Updating system packages..."
dnf update -y

# Java 17 installation
echo "2. Installing Java 17..."
dnf install -y java-17-amazon-corretto-devel
java -version

# Essential tools
echo "3. Installing essential tools..."
dnf install -y wget curl htop vim unzip git

# SSH Key Setup for CI/CD access
echo "3.1. Setting up SSH key for CI/CD access..."
if [ ! -z "${PRIVATE_KEY_CONTENT}" ]; then
    # Extract public key from private key
    echo "${PRIVATE_KEY_CONTENT}" > /tmp/temp_private_key
    chmod 600 /tmp/temp_private_key
    ssh-keygen -y -f /tmp/temp_private_key > /tmp/public_key
    
    # Setup authorized_keys for ec2-user
    mkdir -p /home/ec2-user/.ssh
    cat /tmp/public_key >> /home/ec2-user/.ssh/authorized_keys
    chmod 600 /home/ec2-user/.ssh/authorized_keys
    chmod 700 /home/ec2-user/.ssh
    chown -R ec2-user:ec2-user /home/ec2-user/.ssh
    
    # Cleanup
    rm -f /tmp/temp_private_key /tmp/public_key
    echo "SSH key setup completed for CI/CD access"
else
    echo "WARNING: PRIVATE_KEY_CONTENT not provided. SSH key setup skipped."
fi

# Database and Redis clients for debugging
echo "3.2. Installing database and cache clients..."
dnf install -y mariadb105 redis6

# Install SSM Agent for CI/CD deployment
echo "3.1. Installing SSM Agent..."
dnf install -y amazon-ssm-agent
systemctl enable amazon-ssm-agent
systemctl start amazon-ssm-agent
echo "SSM Agent installation completed"

# Create springboot user
echo "4. Creating springboot user..."
useradd -m -s /bin/bash springboot

# Create directories
echo "5. Creating application directory..."
mkdir -p /opt/empick
mkdir -p /var/log/empick
chown springboot:springboot /opt/empick
chown springboot:springboot /var/log/empick

# Environment variables
echo "6. Setting up environment variables..."
cat > /opt/empick/.env << EOF
# Server Configuration
SERVER_PORT=8080

# Database Configuration
DB_HOST=${DB_HOST}
DB_PORT=${DB_PORT}
DB_NAME=${DB_NAME}
DB_USERNAME=${DB_USERNAME}
DB_PASSWORD=${DB_PASSWORD}

# Redis Configuration
REDIS_HOST=${REDIS_HOST}
REDIS_PORT=${REDIS_PORT}

# JWT Configuration
JWT_SECRET=${JWT_SECRET}

# Email Configuration
MAIL_HOST=${MAIL_HOST}
MAIL_PORT=${MAIL_PORT}
MAIL_USERNAME=${MAIL_USERNAME}
MAIL_PASSWORD=${MAIL_PASSWORD}

# AWS S3 Configuration
AWS_S3_BUCKET=${AWS_S3_BUCKET}
AWS_S3_REGION=${AWS_S3_REGION}
AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
AWS_SECRET_KEY=${AWS_SECRET_KEY}
EOF

# Set permissions
chown springboot:springboot /opt/empick/.env
chmod 600 /opt/empick/.env

# Download JAR file
echo "7. Downloading Spring Boot JAR file..."
cd /opt/empick
if [ ! -z "${S3_JAR_PATH}" ]; then
    sudo -u springboot aws s3 cp "${S3_JAR_PATH}" /opt/empick/empick-backend.jar --region ap-northeast-2
    if [ $? -eq 0 ]; then
        echo "JAR file downloaded successfully"
        chmod +x empick-backend.jar
    else
        echo "ERROR: Failed to download JAR file"
        echo "JAR file not available" > empick-backend.jar
    fi
else
    echo "WARNING: S3_JAR_PATH not provided. Creating placeholder..."
    echo "JAR file not available" > empick-backend.jar
fi

# Create systemd service
echo "8. Creating systemd service..."
cat > /etc/systemd/system/empick-backend.service << EOF
[Unit]
Description=Empick Spring Boot Application
After=network.target

[Service]
Type=simple
User=springboot
Group=springboot
WorkingDirectory=/opt/empick
EnvironmentFile=/opt/empick/.env
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=production -Dlogging.file.name=/var/log/empick/application.log /opt/empick/empick-backend.jar
Restart=always
RestartSec=10
StandardOutput=append:/var/log/empick/stdout.log
StandardError=append:/var/log/empick/stderr.log

# JVM Settings
Environment="JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC -XX:+PrintGC -XX:+PrintGCDetails -Xloggc:/var/log/empick/gc.log"

[Install]
WantedBy=multi-user.target
EOF

# Start service
echo "9. Starting Spring Boot service..."
systemctl daemon-reload
systemctl enable empick-backend.service

# Start service only if valid JAR exists
if [ -s /opt/empick/empick-backend.jar ] && [ "$(file /opt/empick/empick-backend.jar | grep -c 'Java archive')" -gt 0 ]; then
    systemctl start empick-backend.service
    echo "Spring Boot service started successfully"
else
    echo "WARNING: Valid JAR file not found. Service not started."
    echo "Please upload JAR file and run: sudo systemctl start empick-backend.service"
fi

echo "10. Setup completed!"
echo ""
echo "=== Useful Commands ==="
echo "Check service status: sudo systemctl status empick-backend"
echo "View logs: sudo journalctl -u empick-backend -f"
echo "Restart service: sudo systemctl restart empick-backend"
echo ""

echo "=== Spring Boot Auto Deploy Completed at $(date) ===" 