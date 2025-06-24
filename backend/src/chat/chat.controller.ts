import { Controller, Get, Post, Body, UseGuards, Request, Param } from '@nestjs/common';
import { ChatService } from './chat.service';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { CreateMessageDto } from './dto/create-message.dto';

@Controller('chat')
export class ChatController {
  constructor(private readonly chatService: ChatService) {}

  @Get('test')
  async test() {
    return { message: 'Chat controller is working' };
  }

  @UseGuards(JwtAuthGuard)
  @Get('messages')
  async getMessages(@Request() req) {
    return this.chatService.getMessages(req.user);
  }

  @UseGuards(JwtAuthGuard)
  @Post('messages')
  async sendMessage(@Body() createMessageDto: CreateMessageDto, @Request() req) {
    return this.chatService.sendMessage(createMessageDto, req.user);
  }

  @UseGuards(JwtAuthGuard)
  @Get('conversations')
  async getConversations(@Request() req) {
    return this.chatService.getConversations(req.user);
  }

  @UseGuards(JwtAuthGuard)
  @Get('messages/:userId')
  async getMessagesWithUser(@Param('userId') userId: string, @Request() req) {
    return this.chatService.getMessagesWithUser(req.user, userId);
  }
} 