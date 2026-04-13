// selected files holder
let selectedFiles = [];

// Handle files
let handleFiles = function(files) {
    selectedFiles = Array.from(files);
    renderFileList();
    message.classList.remove('success', 'error');
    message.textContent = '';
}

// Show message
let showMessage = function(text, type) {
    message.textContent = text;
    message.classList.add(type);
    setTimeout(() => {
        message.classList.remove(type);
    }, 5000);
}

// Remove file from the list
let removeFile = function(index) {
    selectedFiles.splice(index, 1);
    renderFileList();
}


// Render file list
let renderFileList = function() {

    $("#fileContainer").empty();

    if (selectedFiles.length === 0) {
        $("#fileList").removeClass('show');
        return;
    }

    $("#fileList").removeClass('show');

    selectedFiles.forEach((file, index) => {
      const fileItemName = $('<div></div>')
          .addClass('file-name')
          .text(escapeHtml(file.name));
      const fileItemSize = $('<div></div>')
          .addClass('file-size')
          .text(formatFileSize(file.size));
      const fileItemDetails = $('<div></div>')
          .addClass('file-details')
          .append(fileItemName)
          .append(fileItemSize);
      const fileItemInfo = $('<div></div>')
          .addClass('file-info')
          .append(fileItemDetails);
      const fileItemRemove = $('<button></button>')
          .addClass('remove-btn')
          .text('Remove')
          .on('click', function() {
              removeFile(index);
          });
      const fileItem = $('<div></div>')
          .addClass('file-item')
          .append(fileItemInfo)
          .append(fileItemRemove);

      $("#fileContainer").append(fileItem);
      $("#fileList").addClass('show');
        
    });
}


// Format file size
let formatFileSize = function(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
}

// Escape HTML
let escapeHtml = function(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// file upload progress
let fileUploadProgress = function(event) {
  if (event.lengthComputable) {
    const percentComplete = (event.loaded / event.total) * 100;
    $("#progressFill").css('width', percentComplete + '%');
  }
}

// file upload successful
let fileUploadSuccessful = function(data) {
  showMessage('Files uploaded successfully!', 'success');
  selectedFiles = [];
  renderFileList();
  $("#fileInput").val('');
  $("#progressFill").css('width', '0%');
  $("#submitBtn").prop('disabled', false);
  $("#loading").removeClass('show');
}

// file upload error handler
let fileUploadError = function(xhr, textStatus, exception) {
  $("#loading").removeClass('show');
  $("#submitBtn").prop('disabled', false);
  
  if (xhr.status === 0) {
    showMessage('Not connect. Verify Network.', 'error');
  } else if (xhr.status == 404) {
    showMessage('Requested page not found (404).', 'error');
  } else if (xhr.status == 500) {
    showMessage('Internal Server Error (500).', 'error');
  } else if (exception === 'timeout') {
    showMessage('Time out error.', 'error');
  } else if (exception === 'abort') {
    showMessage('Ajax request aborted.', 'error');
  } else {
    showMessage('Uncaught Error. ' + xhr.responseText, 'error');
  }
}

// display upload progress
let displayUploadProgress = function() {
  const xhr = $.ajaxSettings.xhr();

  if (xhr.upload) {
    xhr.upload.addEventListener('progress', fileUploadProgress, false);
  }
  return xhr;
}

// upload handler
let fileUploadHandler = async function(event) {
  event.preventDefault();

  if( selectedFiles.length === 0 ) {
    showMessage('Please select at least one file', 'error');
    return;
  }
  
  const formData = new FormData();
  selectedFiles.forEach(file => {
      formData.append('files', file);
  });

  $("#submitBtn").prop('disabled', true);
  $("#loading").addClass('show');
  $("#message").text('');
  $("#message").removeClass('success error');

  $.ajax({
    method: 'POST',
    url: '/rag/upload',
    data: formData,
    cache: false,
    contentType: false,
    processData: false,
    xhr: displayUploadProgress
  })
  .done(fileUploadSuccessful)
  .fail(fileUploadError);
}

// main section 
$(document).ready(function() {

  // upload file  
  $("#uploadArea").on("click", function(e) {
    e.preventDefault();
    $("#fileInput").focus().trigger("click");
  });

  // Handle drag and drop
  $("#uploadArea").on('dragover', function(e) {
      e.preventDefault();
      $(this).addClass('dragover');
  });
  $("#uploadArea").on('dragleave', function() {
      $(this).removeClass('dragover');
  });
  $("#uploadArea").on('drop', function(e) {
      e.preventDefault();
      $(this).removeClass('dragover');
      handleFiles(e.dataTransfer.files);
  });

  // Handle file input change
  $("#fileInput").on("change", function(e) {
      handleFiles(e.target.files);
  });
  
  // Handle form submit
  $("#uploadForm").on("submit", fileUploadHandler);

});


