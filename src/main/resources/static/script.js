const API_BASE_URL = 'http://localhost:8080/api';

// Check AI status on page load
window.addEventListener('DOMContentLoaded', checkAIStatus);

// Enter key to send message
document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('userInput');
    input.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });
});

async function checkAIStatus() {
    try {
        const response = await fetch(`${API_BASE_URL}/ai/status`);
        const data = await response.json();
        
        const statusDot = document.querySelector('.status-dot');
        const statusText = document.getElementById('status-text');
        
        if (data.ollamaAvailable) {
            statusDot.classList.remove('offline');
            statusDot.classList.add('online');
            statusText.textContent = 'AI Ready';
        } else {
            statusDot.classList.remove('online');
            statusDot.classList.add('offline');
            statusText.textContent = 'AI Offline';
            
            addMessage('ai', 'Warning: Ollama is not running. Please start Ollama first to use AI features.');
        }
    } catch (error) {
        console.error('Failed to check AI status:', error);
        const statusText = document.getElementById('status-text');
        statusText.textContent = 'Connection Error';
    }
}

function quickMessage(message) {
    document.getElementById('userInput').value = message;
    sendMessage();
}

async function sendMessage() {
    const input = document.getElementById('userInput');
    const message = input.value.trim();
    
    if (!message) return;
    
    // Add user message to chat
    addMessage('user', message);
    input.value = '';
    
    // Disable send button
    const sendButton = document.getElementById('sendButton');
    const buttonText = document.getElementById('buttonText');
    const buttonLoader = document.getElementById('buttonLoader');
    
    sendButton.disabled = true;
    buttonText.classList.add('hidden');
    buttonLoader.classList.remove('hidden');
    
    try {
        const response = await fetch(`${API_BASE_URL}/ai/chat`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ message })
        });
        
        if (!response.ok) {
            throw new Error('Failed to send message');
        }
        
        const data = await response.json();
        
        // Add AI response
        addMessage('ai', data.message, data.data);
        
    } catch (error) {
        console.error('Error:', error);
        addMessage('ai', 'Sorry, may error sa pag-send ng message. Please check if the server is running.');
    } finally {
        // Re-enable send button
        sendButton.disabled = false;
        buttonText.classList.remove('hidden');
        buttonLoader.classList.add('hidden');
    }
}

function addMessage(sender, text, data = null) {
    const messagesContainer = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message`;
    
    const contentDiv = document.createElement('div');
    contentDiv.className = 'message-content';
    
    const textP = document.createElement('p');
    textP.textContent = text;
    contentDiv.appendChild(textP);
    
    // Add data preview if available
    if (data) {
        const dataDiv = document.createElement('div');
        dataDiv.className = 'message-data';
        
        if (Array.isArray(data)) {
            dataDiv.innerHTML = `<strong>Results (${data.length} items):</strong><br><pre>${JSON.stringify(data, null, 2)}</pre>`;
        } else if (typeof data === 'object') {
            dataDiv.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
        } else {
            dataDiv.textContent = data;
        }
        
        contentDiv.appendChild(dataDiv);
    }
    
    messageDiv.appendChild(contentDiv);
    messagesContainer.appendChild(messageDiv);
    
    // Scroll to bottom
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}
