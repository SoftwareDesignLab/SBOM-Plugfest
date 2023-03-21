package plugfest.tooling.metrics;

/**
 * Imports from CycloneDX Java Core Libraries
 */
import org.cyclonedx.BomGeneratorFactory;
import org.cyclonedx.BomParserFactory;
import org.cyclonedx.CycloneDxMediaType;
import org.cyclonedx.CycloneDxSchema;

/**
 * Import from CycloneDX Java Exception Libraries
 */
import org.cyclonedx.exception.BomLinkException;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.exception.ParseException;

/**
 * Import from CycloneDX Java Generators XML Libraries
 */
import org.cyclonedx.generators.xml.AbstractBomXmlGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator10;
import org.cyclonedx.generators.xml.BomXmlGenerator11;
import org.cyclonedx.generators.xml.BomXmlGenerator12;
import org.cyclonedx.generators.xml.BomXmlGenerator13;
import org.cyclonedx.generators.xml.BomXmlGenerator14;

/**
 * Import from CycloneDX Java Generators JSON Libraries
 */
import org.cyclonedx.generators.json.AbstractBomJsonGenerator;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.json.BomJsonGenerator12;
import org.cyclonedx.generators.json.BomJsonGenerator13;
import org.cyclonedx.generators.json.BomJsonGenerator14;

/**
 * Import from CycloneDX Java Model Libraries
 */
import org.cyclonedx.model.Ancestors;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.Attribute;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.BomReference;
import org.cyclonedx.model.Commit;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ComponentWrapper;
import org.cyclonedx.model.Composition;
import org.cyclonedx.model.Copyright;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.DependencyList;
import org.cyclonedx.model.Descendants;
import org.cyclonedx.model.Diff;
import org.cyclonedx.model.Evidence;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.Extension;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.IdentifiableActionType;
import org.cyclonedx.model.Issue;
import org.cyclonedx.model.JsonOnly;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Patch;
import org.cyclonedx.model.Pedigree;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.ReleaseNotes;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.ServiceData;
import org.cyclonedx.model.Signature;
import org.cyclonedx.model.Source;
import org.cyclonedx.model.Swid;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.Variants;
import org.cyclonedx.model.VersionFilter;
import org.cyclonedx.model.XmlOnly;

/**
 * Import from CycloneDX Java Model Vulnerability Libraries
 */
import org.cyclonedx.model.vulnerability.Rating;
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.model.vulnerability.Vulnerability10;

/**
 * Import from CycloneDX Java Parsers Libraries
 */
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.cyclonedx.parsers.XmlParser;

/**
 * Import from CycloneDX Java Utility Libraries
 */
import org.cyclonedx.util.BomLink;
import org.cyclonedx.util.BomUtils;
import org.cyclonedx.util.CollectionTypeSerializer;
import org.cyclonedx.util.ComponentWrapperDeserializer;
import org.cyclonedx.util.ComponentWrapperSerializer;
import org.cyclonedx.util.CustomDateSerializer;
import org.cyclonedx.util.DependencyDeserializer;
import org.cyclonedx.util.DependencySerializer;
import org.cyclonedx.util.ExtensibleTypesSerializer;
import org.cyclonedx.util.ExtensionDeserializer;
import org.cyclonedx.util.ExtensionSerializer;
import org.cyclonedx.util.ExternalReferenceSerializer;
import org.cyclonedx.util.LicenseChoiceSerializer;
import org.cyclonedx.util.LicenseDeserializer;
import org.cyclonedx.util.LicenseResolver;
import org.cyclonedx.util.ObjectLocator;
import org.cyclonedx.util.TrimStringSerializer;
import org.cyclonedx.util.VersionJsonAnnotationIntrospector;
import org.cyclonedx.util.VersionXmlAnnotationIntrospector;
import org.cyclonedx.util.VulnerabilityDeserializer;

/**
 * Import from CycloneDX Java Utility Mixin Libraries
 */
import org.cyclonedx.util.mixin.MixInBomReference;


public class CDXMetrics {
    
}
