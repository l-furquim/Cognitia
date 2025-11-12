resource "aws_ecs_cluster" "videos_processing_cluster" {
  name = "videos_processing_cluster"
}

resource "aws_iam_role" "video_processing_role" {
  name = "video_processing_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid = ""
        Principal = {
          Service = "ecs.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_ecs_task_definition" "videos_processing_task" {
  family = "video_processing"
  network_mode = "awsvpc"
  requires_compatibilities = ["fargate"]
  cpu = "512"
  memory = "1024"

  container_definitions = jsonencode([
    {
      name = "video-processor"
      image = ""
      essential = true
      environment = [
        { name = "S3_BUCKET", value = aws_s3_bucket.videos_bucket }
      ]
    }
  ])
  execution_role_arn = aws_iam_role.video_processing_role.arn
  task_role_arn      = aws_iam_role.ecs_task_role.arn
}