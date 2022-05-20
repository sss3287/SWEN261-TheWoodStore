import { Injectable } from '@angular/core';

/**
 * shows messages from other services
 * can clear all displayed messages
 */
@Injectable({
  providedIn: 'root',
})
export class MessageService {
  messages: string[] = [];

  add(message: string) {
    this.messages.push(message);
  }

  clear() {
    this.messages = [];
  }
}