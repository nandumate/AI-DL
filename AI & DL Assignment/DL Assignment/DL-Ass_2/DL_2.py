import cv2

# Initialize face detector
faceCascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

# Start video capture
video_capture = cv2.VideoCapture(0)

while True:
    # Capture frame-by-frame
    ret, frame = video_capture.read()
    
    # Detect faces and draw rectangles
    faces = faceCascade.detectMultiScale(cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY), 1.1, 5)
    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 255, 255), 2)
    
    # Display the resulting frame
    cv2.imshow('Face Detection', frame)
    
    # Exit on pressing 'q'
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release resources
video_capture.release()
cv2.destroyAllWindows()


