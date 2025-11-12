resource "aws_s3_bucket" "videos_bucket" {
  bucket = var.s3_bucket_name
}

resource "aws_s3_bucket_notification" "videos_notification" {
  bucket = aws_s3_bucket.videos_bucket.id

  eventbridge = {
    event_bridge_enable = true
  }
}