resource "aws_cloudwatch_event_rule" "s3_video_upload" {
  name = "on-video-upload"
  description = "Detect if any raw video is uploaded for processing"
  event_pattern = jsonencode({
    source = ["aws.s3"],
    detail_type = ["Object Created"],
    detail = {
      bucket = { name = [aws_s3_bucket.videos_bucket.bucket] }
      object = { key = [{ prefix = "input/" }, { suffix = ".mp4" }] } 
    }
  })
}

resource "aws_cloudwatch_event_target" "s3_video_upload_target" {
  rule = aws_cloudwatch_event_rule.s3_video_upload.name
  arn = aws_ecs_cluster.videos_processing_cluster.arn
  role_arn = aws_iam_role.video_processing_role.arn


  ecs_target {
    task_definition_arn = aws_ecs_task_definition.videos_processing_task.arn
    launch_type = "FARGATE"

    network_configuration {
      awsvpc_configuration {
        subnets = [aws_subnet.public.id]
        assign_public_ip = "ENABLED"
      }
    }
  }
}