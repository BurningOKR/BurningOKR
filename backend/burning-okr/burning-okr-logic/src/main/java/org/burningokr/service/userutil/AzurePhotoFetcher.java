package org.burningokr.service.userutil;

import org.burningokr.service.condition.AadCondition;
import org.burningokr.service.exceptions.AzureUserFetchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

@Conditional(AadCondition.class)
@Service
public class AzurePhotoFetcher {

  private AzureApiCaller azureApiCaller;

  @Autowired
  public AzurePhotoFetcher(AzureApiCaller azureApiCaller) {
    this.azureApiCaller = azureApiCaller;
  }

  String getPhotoForUserId(String auth, UUID userId) throws AzureUserFetchException {
    try {
      URL url = new URL("https://graph.microsoft.com/v1.0/users/" + userId + "/photo/$value");
      InputStream inputStream = azureApiCaller.callApi(auth, url);
      BufferedImage photo = getPhotoAsImage(inputStream);
      photo = resizeImage(photo, 240, 240);
      return convertImageToBase64(photo);
    } catch (IOException e) {
      throw new AzureUserFetchException(e.getMessage());
    } catch (IllegalStateException e) {
      return null; // No photo found for the user.
    }
  }

  private BufferedImage getPhotoAsImage(InputStream inputStream) throws IOException {
    return ImageIO.read(inputStream);
  }

  private BufferedImage resizeImage(BufferedImage imageToScale, int width, int height) {
    BufferedImage scaledImage = null;
    if (imageToScale != null) {
      scaledImage = new BufferedImage(width, height, imageToScale.getType());
      Graphics2D graphics2D = scaledImage.createGraphics();
      graphics2D.setRenderingHint(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphics2D.setRenderingHint(
          RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphics2D.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics2D.drawImage(imageToScale, 0, 0, width, height, null);
      graphics2D.dispose();
    }
    return scaledImage;
  }

  private String convertImageToBase64(BufferedImage image) throws IOException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      ImageIO.write(image, "jpg", byteArrayOutputStream);
      byte[] imageBytes = byteArrayOutputStream.toByteArray();

      return new String(Base64.getEncoder().encode(imageBytes));
    }
  }
}
