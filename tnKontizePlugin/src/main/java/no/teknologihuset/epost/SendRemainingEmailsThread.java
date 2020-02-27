package no.teknologihuset.epost;

import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.spi.StoragePlugin;
import no.teknologihuset.handlers.BookingInquiryAssembler;
import no.teknologihuset.handlers.data.BookingInquiry;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by jhsmbp on 19/02/14.
 */
public class SendRemainingEmailsThread implements Runnable {
    private static final Logger logger = Logger.getLogger(SendRemainingEmailsThread.class.getName());

    private StoragePlugin storagePlugin;
    private String host;

    public SendRemainingEmailsThread(String host, StoragePlugin storagePlugin) {
        this.storagePlugin = storagePlugin;
        this.host = host;
    }

    @Override
    public void run() {
        logger.info("Sending remaining emails");

        List<SubCategoryData> remainingEmails = storagePlugin.getSubCategories(host, "emailsNotSent");

        for (SubCategoryData subCategoryData : remainingEmails) {
            BookingInquiry bookingInquiry = BookingInquiryAssembler.convertSubCategoryToBookingInquiry(subCategoryData);

            EpostThread epostThread = new EpostThread(host, bookingInquiry, storagePlugin);
            epostThread.run();
        }
    }
}
