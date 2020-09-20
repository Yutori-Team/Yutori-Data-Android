import cv2
import numpy as np
from matplotlib import pyplot as plt

# img = cv2.imread('../image-data/hangul-images/hangul_1.jpeg') 
img = cv2.imread("C:\\Users\\LEE\\Desktop\\Yutori-Data-Android\\sample\\sample1.jpeg")
rows, cols, ch = img.shape

pts1 = np.float32([[20,10],[40,10],[20,20]]) # 오른쪽 디폴트
pts2 = np.float32([[20,20],[40,10],[20,30]]) # 오른쪽 위
# pts2 = np.float32([[20,10],[40,20],[20,20]]) # 오른쪽 아래


M = cv2.getAffineTransform(pts1, pts2)

dst = cv2.warpAffine(img, M, (cols,rows))

plt.subplot(121),plt.imshow(img),plt.title('image')
plt.subplot(122),plt.imshow(dst),plt.title('Affine')
plt.show()