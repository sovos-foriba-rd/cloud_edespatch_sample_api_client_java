# Foriba Bulut e-İrsaliye API Java Test Projesi

Bu proje Foriba Bulut e-İrsaliye API web servis metodlarının nasıl kullanılması gerektiği ile ilgili örnek olması için oluşturulmuştur. Proje yalnızca  test sisteminde çalışmakta ve web servislere bağlantı ayarları da projede bulunmaktadır.

 **e-İrsaliye Ürünü İçin:**

- Kullanıcı listesinin indirilmesi.
- Belge gönderimi.
- Gelen giden belge listesi alımı.
- Yüklenen belgenin indirilmesi.
- Gelen ve giden belgelerin pdf,html,xslt alınabilir.
- Yüklenen zarfların statülerinin sorgulanması. 
- Uygulama yanıtının sorgulanması.


işlemleri yapılmaktadır.

Web servis erişim güvenliği basic authentication ile sağlanmaktadır. Web servisleri kullanacak istemcilerin Foriba Bulut e-İrsaliye Portal test sistemi kullanıcı adı ve şifresine sahip olmaları gerekmektedir. Bu kullanıcı adı ve şifre ile web service doğrulaması gerçekleştirilebilir.


## Kurulum

Bu proje Eclipse geliştirme ortamında Java yazılım dili standartları ile oluşturulmuştur.

ClientDespatch projesinin test edilmesi:

- İndirilen proje File ->İmport ->Maven -> Existing Maven Projects  üzerinden import edilir.
- sendUBL ve sendRAUBL methodu için örnek envDesUBL.xml,envRAUBL.xml irsaliye verileri com.foriba.eDespatch.test package altında bulunmaktadır.
- Foriba Bulut e-İrsaliye Portal test sistemi kullanıcı adı ve şifresi, resources altındaki user.properties içerisine username ve password alanlarına girilmelidir.
- Application.java classında main methodunda içerisinde bulunan WS methodlarını kullanmak için gerekli bilgiler girilerek getDesUserList,sendDesUBL,getDesUBL,getDesUBLList,getDesEnvelopeStatus,getDesView WS methodları test edilebilir.



# Lisans
  
Foriba Bulut API Java Test Projesi, **Foriba R&D** ekibi tarafından API kullanımını anlatmak için hazırlanmıştır, izinsiz olarak ticari uygulamalarda kullanılması yasaktır.  
