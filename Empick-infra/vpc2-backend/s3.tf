# ================================================================================
# S3 Configuration for Empick Backend
# 이 파일은 Spring Boot 애플리케이션을 위한 S3 버킷과 접근 권한을 설정합니다.
# ================================================================================

# S3 Bucket for Empick Backend
# JAR 파일, 로그, 백업 파일 등을 저장하는 메인 버킷
resource "aws_s3_bucket" "empick_bucket" {
  bucket = var.s3_bucket_name

  tags = merge(var.common_tags, {
    Name        = "empick-private-bucket"
    Type        = "Storage"
    Purpose     = "jar-files-logs-backups"
    Environment = var.environment
  })
}

# S3 Bucket Versioning
# JAR 파일의 이전 버전을 보관하여 롤백 가능
resource "aws_s3_bucket_versioning" "empick_bucket_versioning" {
  bucket = aws_s3_bucket.empick_bucket.id
  versioning_configuration {
    status = "Enabled"
  }
}

# S3 Bucket Public Access Block
# 보안을 위해 퍼블릭 액세스 차단
resource "aws_s3_bucket_public_access_block" "empick_bucket_pab" {
  bucket = aws_s3_bucket.empick_bucket.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

# S3 Bucket Server-Side Encryption
# 저장된 파일의 보안을 위한 암호화 설정
resource "aws_s3_bucket_server_side_encryption_configuration" "empick_bucket_encryption" {
  bucket = aws_s3_bucket.empick_bucket.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
    bucket_key_enabled = true
  }
}

# S3 Bucket Lifecycle Configuration
# 오래된 파일 자동 정리로 비용 절약
resource "aws_s3_bucket_lifecycle_configuration" "empick_bucket_lifecycle" {
  bucket = aws_s3_bucket.empick_bucket.id

  rule {
    id     = "cleanup_old_versions"
    status = "Enabled"

    filter {
      prefix = "releases/"
    }

    # 이전 버전 JAR 파일 30일 후 삭제
    noncurrent_version_expiration {
      noncurrent_days = 30
    }

    # 로그 파일 90일 후 삭제 
    expiration {
      days = 90
    }
  }

  rule {
    id     = "cleanup_temp_files"
    status = "Enabled"

    filter {
      prefix = "temp/"
    }

    # 임시 파일 7일 후 삭제
    expiration {
      days = 7
    }
  }
}

# S3 Bucket Policy for EC2 Access
# EC2 인스턴스(Spring Boot)가 S3 버킷에 접근할 수 있도록 권한 부여
resource "aws_s3_bucket_policy" "empick_bucket_policy" {
  bucket = var.s3_bucket_name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          AWS = aws_iam_role.springboot_ec2_role.arn
        }
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject",
          "s3:ListBucket"
        ]
        Resource = [
          "arn:aws:s3:::${var.s3_bucket_name}",
          "arn:aws:s3:::${var.s3_bucket_name}/*"
        ]
      }
    ]
  })
}

# ================================================================================
# 💡 S3 버킷 정책 설명:
# 
# 🎯 목적:
# - EC2 인스턴스의 IAM 역할이 S3 버킷에 접근할 수 있도록 권한 부여
# - JAR 파일 다운로드, 파일 업로드/다운로드 등을 위한 필수 설정
# 
# 🔐 권한:
# - s3:GetObject: JAR 파일 및 기타 파일 다운로드
# - s3:PutObject: 파일 업로드 (로그, 백업 등)
# - s3:DeleteObject: 임시 파일 정리
# - s3:ListBucket: 버킷 내 파일 목록 조회
# 
# 🔗 연동:
# - ec2-springboot.tf의 aws_iam_role.springboot_ec2_role과 연결
# - terraform.tfvars의 s3_bucket_name 변수 사용
# 
# ⚠️ 주의사항:
# - 이 정책이 없으면 EC2에서 S3 접근 시 403 Forbidden 에러 발생
# - GitHub Actions에서 S3 업로드는 되지만 EC2에서 다운로드 실패
# ================================================================================ 
