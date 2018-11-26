import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProgressService {

  private working = false;

  constructor() {
  }

  getWorking(): boolean {
    return this.working;
  }

  startWorking(): void {
    this.working = true;
  }

  finishWorking(): void {
    this.working = false;
  }
  
}
